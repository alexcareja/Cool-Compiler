package cool.ast;

import cool.compiler.Compiler;
import cool.parser.CoolParser;
import cool.structures.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CodeGenVisitor implements ASTVisitor<ST> {
    private final STGroupFile templates;
    Scope currentScope;
    private final ST methodsST;
    private final ST constantsST;
    private final ST classNamesST;
    private final ST classObjsST;
    private final ST protObjsST;
    private final ST dispatchTablesST;
    ArrayList<Integer> integers = new ArrayList<>();
    ArrayList<String> strings = new ArrayList<>();
    private int caseIndex = 0;
    private int caseBranchIndex = 0;
    private int ifIndex = 0;
    private int whileIndex = 0;
    private int isVoidIndex = 0;
    private int notIndex = 0;
    private int eqIndex = 0;
    private int relationalIndex = 0;
    private int dispatchIndex = 0;

    public CodeGenVisitor() {
        this.templates = new STGroupFile("cgen.stg");
        this.methodsST = templates.getInstanceOf("sequenceSpaced");
        this.constantsST = templates.getInstanceOf("sequence");
        this.classNamesST = templates.getInstanceOf("sequence");
        this.classObjsST = templates.getInstanceOf("sequence");
        this.protObjsST = templates.getInstanceOf("sequence");
        this.dispatchTablesST = templates.getInstanceOf("sequence");

        Primitive.OBJECT.setTag();
        getIntLabel(0);
        getStringLabel("");
        initBoolLabel();

        methodsST.add("e", Primitive.OBJECT.constructor(templates))
                .add("e", Primitive.IO.constructor(templates))
                .add("e", Primitive.STRING.constructor(templates))
                .add("e", Primitive.INT.constructor(templates))
                .add("e", Primitive.BOOL.constructor(templates));

        ArrayList<Symbol> classes = new ArrayList<>(((DefaultScope) SymbolTable.globalScope).getSymbols().values());
        classes.sort(Comparator.comparingInt(p -> ((TypeSymbol) p).getTag()));

        for (Symbol s : classes) {
            TypeSymbol type = (TypeSymbol) s;
            if (type != Primitive.SELF_TYPE) {
                getIntLabel(type.getName().length());
                classNamesST.add("e", "\t.word " + getStringLabel(type.getName()));
                dispatchTablesST.add("e", type.dispatchTable(templates));
                protObjsST.add("e", type.protObj(templates));
                classObjsST.add("e", templates.getInstanceOf("protObjTabField")
                        .add("class", s.getName()));
            }
        }
    }

    @Override
    public ST visit(Assign assign) {
        IdSymbol idSymbol = (IdSymbol) currentScope.lookup(assign.getId());
        ST valueST = assign.getValue().accept(this);
        ST st = idSymbol.isAttribute() ?
                templates.getInstanceOf("setAttribute") :
                templates.getInstanceOf("setLetVar");
                
        return st.add("value", valueST)
                .add("offset", idSymbol.getOffset());
    }

    @Override
    public ST visit(StaticDispatch staticDispatch) {
        if (staticDispatch.getCallerType() == null) {
            return null;
        }
        ST st = templates.getInstanceOf("staticDispatch")
                .add("method", staticDispatch.getId())
                .add("index", dispatchIndex++)
                .add("filename", getFileName(staticDispatch.getContext()))
                .add("offset", staticDispatch.getCallerType().lookupMethod(staticDispatch.getId()).getOffset())
                .add("line", staticDispatch.getIdToken().getLine());

        if (staticDispatch.getInvoker() instanceof Id) {
            if (!staticDispatch.getInvoker().getToken().getText().equals("self")) {
                st.add("invoker", staticDispatch.getInvoker().accept(this));
            }
        } else {
            st.add("invoker", staticDispatch.getInvoker().accept(this));
        }
        st.add("args", extractArgs(staticDispatch.getArgs()));
        if (staticDispatch.hasType()) {
            st.add("callerType", staticDispatch.getType());
        }

        return st;
    }

    @Override
    public ST visit(Dispatch dispatch) {
        if (dispatch.getCallerType() == null) {
            return null;
        }
        return templates.getInstanceOf("dispatch")
                .add("method", dispatch.getId())
                .add("index", dispatchIndex++)
                .add("args", extractArgs(dispatch.getArgs()))
                .add("offset", dispatch.getCallerType().lookupMethod(dispatch.getId()).getOffset())
                .add("filename", getFileName(dispatch.getContext()))
                .add("line", dispatch.getIdToken().getLine());
    }

    @Override
    public ST visit(If iff) {
        return templates.getInstanceOf("if")
                .add("cond", iff.getCond().accept(this))
                .add("then", iff.getThenBranch().accept(this))
                .add("elsee", iff.getElseBranch().accept(this))
                .add("index", ifIndex++);
    }

    @Override
    public ST visit(While whilee) {
        ST body = templates.getInstanceOf("sequence");

        for (Expression e : whilee.getBody()) {
            body.add("e", e.accept(this));
        }

        return templates.getInstanceOf("while")
                .add("cond", whilee.getCond().accept(this))
                .add("body", body)
                .add("index", whileIndex++);
    }

    @Override
    public ST visit(Block block) {
        ST st = templates.getInstanceOf("sequence");

        for (Expression e : block.getBody()) {
            st.add("e", e.accept(this));
        }

        return st;
    }

    @Override
    public ST visit(Let let) {
        ST letST = templates.getInstanceOf("sequence");
        ST initLetST = templates.getInstanceOf("defineLet")
                .add("size", -let.getVars().size() * 4);
        int offset = -4;

        letST.add("e", initLetST);

        currentScope = let.getScope();

        for (VarDef vd : let.getVars()) {
            vd.getIdSymbol().setOffset(offset);
            letST.add("e", vd.accept(this));
            offset -= 4;
        }

        ST st = letST.add("e", let.getBody().accept(this))
                .add("e", templates.getInstanceOf("defineLet")
                        .add("size", let.getVars().size() * 4));

        currentScope = currentScope.getParent();

        return st;
    }

    @Override
    public ST visit(CaseDef caseDef) {
        currentScope = caseDef.getScope();

        ST st = templates.getInstanceOf("caseBranch")
                .add("expr", caseDef.getBody().accept(this))
                .add("tag", caseDef.getTypeSymbol().getTag())
                .add("maxTag", caseDef.getTypeSymbol().getMaxTag())
                .add("caseIndex", caseIndex)
                .add("branchIndex", caseBranchIndex++);

        currentScope = currentScope.getParent();

        return st;
    }

    @Override
    public ST visit(Case casee) {
        ArrayList<CaseDef> cases = casee.getCases();
        StringBuilder branches = new StringBuilder();

        cases.sort(Comparator.comparingInt(p -> -p.getTypeSymbol().getTag()));

        for (CaseDef cd : cases) {
            ST st = cd.accept(this);
            if (st != null) {
                branches.append(st.render());
                branches.append("\n");
            }
        }

        return templates.getInstanceOf("defineCase")
                .add("expr", casee.getValue().accept(this))
                .add("branches", branches)
                .add("index", caseIndex++)
                .add("filename", getFileName(casee.getContext()))
                .add("line", casee.getToken().getLine());
    }

    @Override
    public ST visit(New neww) {
        String type = neww.getType();

        if (type.equals("SELF_TYPE")) {
            return templates.getInstanceOf("newSelfType");
        }

        return templates.getInstanceOf("new")
                .add("type", type);
    }

    @Override
    public ST visit(VoidCheck voidCheck) {
        return templates.getInstanceOf("voidCheck")
                .add("expr", voidCheck.value.accept(this))
                .add("index", isVoidIndex++);
    }

    @Override
    public ST visit(Arithmetic arithmetic) {
        String op = "add";

        switch (arithmetic.getOp()) {
            case "+" -> op = "add";
            case "-" -> op = "sub";
            case "*" -> op = "mul";
            case "/" -> op = "div";
            default -> {
            }
        }

        return templates.getInstanceOf("arithmetic")
                .add("leftOp", arithmetic.getLeftOp().accept(this))
                .add("rightOp", arithmetic.getRightOp().accept(this))
                .add("op", op);
    }

    @Override
    public ST visit(Neg neg) {
        return templates.getInstanceOf("neg")
                .add("expr", neg.value.accept(this));
    }

    @Override
    public ST visit(Relational relational) {
        if (relational.getOp().equals("=")) {
            return templates.getInstanceOf("equal")
                    .add("leftOp", relational.getLeftOp().accept(this))
                    .add("rightOp", relational.getRightOp().accept(this))
                    .add("index", eqIndex++);
        }
        String op = relational.getOp().equals("<") ? "blt" : "ble";

        return templates.getInstanceOf("compare")
                .add("leftOp", relational.getLeftOp().accept(this))
                .add("rightOp", relational.getRightOp().accept(this))
                .add("op", op)
                .add("index", relationalIndex++);
    }

    @Override
    public ST visit(Not not) {
        return templates.getInstanceOf("not")
                .add("expr", not.value.accept(this))
                .add("index", notIndex++);
    }

    @Override
    public ST visit(Id id) {
        if (id.getId().equals("self")) {
            return templates.getInstanceOf("self");
        }
        ST st;
        IdSymbol idSymbol = (IdSymbol) currentScope.lookup(id.getId());
        if (idSymbol.isAttribute()) {
            st = templates.getInstanceOf("getAttribute")
                    .add("offset", idSymbol.getOffset());

        } else {
            st = templates.getInstanceOf("getLetVar")
                    .add("offset", idSymbol.getOffset());
        }
        return st;
    }

    @Override
    public ST visit(Int i) {
        return templates.getInstanceOf("defineName")
                .add("full_tag", getIntLabel(Integer.parseInt(i.token.getText())));
    }

    @Override
    public ST visit(Stringg s) {
        return templates.getInstanceOf("defineName")
                .add("full_tag", getStringLabel(s.getToken().getText()));
    }

    @Override
    public ST visit(Bool b) {
        String bool = b.token.getText().equals("true") ? "1" : "0";

        return templates.getInstanceOf("defineName")
                .add("full_tag", "bool_const_" + bool);
    }

    @Override
    public ST visit(Formal formal) {
        return null;
    }

    @Override
    public ST visit(VarDef vardef) {
        IdSymbol idSymbol = vardef.getIdSymbol();
        TypeSymbol type = idSymbol.getType();
        String mips = "\tli $a0 0";

        if (vardef.hasInitValue()) {
            mips = vardef.getValue().accept(this).render();
        } else {
            if (type == Primitive.INT) {
                mips = "\tla $a0 int_const_0";
            }
            if (type == Primitive.BOOL) {
                mips = "\tla $a0 bool_const_0";
            }
            if (type == Primitive.STRING) {
                mips = "\tla $a0 str_const_0";
            }
        }
        return templates.getInstanceOf("setLetVar")
                .add("value", mips)
                .add("offset", idSymbol.getOffset());
    }

    @Override
    public ST visit(ClassElement classElement) {
        if (classElement.isMethod()) {
            ST body = templates.getInstanceOf("sequence");

            currentScope = classElement.getMethodScope();
            for (Expression e : classElement.getBody()) {
                body.add("e", e.accept(this));
            }
            ST st = templates.getInstanceOf("method")
                    .add("name", classElement.getName())
                    .add("body", body)
                    .add("offset", classElement.getParams().size() * 4 + 12);
            currentScope = currentScope.getParent();

            return st;

        } else {
            if (classElement.hasInitValue()) {
                return templates.getInstanceOf("setAttribute")
                        .add("value", classElement.getValue().accept(this))
                        .add("offset", classElement.getIdSymbol().getOffset());
            }
        }
        return null;
    }

    @Override
    public ST visit(ClassDef classDef) {
        currentScope = classDef.getScope();
        methodsST.add("e", createConstructor(classDef));

        for (ClassElement ce : classDef.getBody()) {
            if (ce.isMethod()) {
                ST st = ce.accept(this);
                st.add("class", classDef.getType());
                methodsST.add("e", st);
            }
        }
        return null;
    }

    @Override
    public ST visit(Prog prog) {
        ST intTag = templates.getInstanceOf("defineTag")
                .add("name", "int")
                .add("tag", Primitive.INT.getTag());
        ST boolTag = templates.getInstanceOf("defineTag")
                .add("name", "bool")
                .add("tag", Primitive.BOOL.getTag());
        ST stringTag = templates.getInstanceOf("defineTag")
                .add("name", "string")
                .add("tag", Primitive.STRING.getTag());
        ST tags = templates.getInstanceOf("sequence")
                .add("e", intTag)
                .add("e", boolTag)
                .add("e", stringTag);

        for (ClassDef cd : prog.getClasses()) {
            cd.accept(this);
        }
        return templates.getInstanceOf("prog")
                .add("tags", tags)
                .add("constants", constantsST)
                .add("classNames", classNamesST)
                .add("classObjs", classObjsST)
                .add("protObjs", protObjsST)
                .add("dispTabs", dispatchTablesST)
                .add("methods", methodsST);
    }

    public ST createConstructor(ClassDef classDef) {
        StringBuilder attributes = new StringBuilder();
        TypeSymbol scope = classDef.getScope();

        for (ClassElement ce : classDef.getBody()) {
            if (!ce.isMethod()) {
                ST st = ce.accept(this);
                if (st != null) {
                    attributes.append(st.render());
                    attributes.append("\n");
                }
            }
        }

        return templates.getInstanceOf("constructor")
                .add("class", scope)
                .add("parent", scope.getParentName())
                .add("attributes", attributes);
    }

    public String getIntLabel(int x) {
        if (!integers.contains(x)) {
            integers.add(x);

            ST st = templates.getInstanceOf("defineIntConst")
                    .add("tag", Primitive.INT.getTag())
                    .add("value", x);
            constantsST.add("e", st);
        }

        return "int_const_" + x;
    }

    public void initBoolLabel() {
        ST st = templates.getInstanceOf("defineBoolConst")
                .add("tag", Primitive.BOOL.getTag());

        constantsST.add("e", st);
    }

    public String getStringLabel(String s) {
        int index;

        if (strings.contains(s)) {
            index = strings.indexOf(s);
        } else {
            index = strings.size();
            strings.add(s);

            ST st = templates.getInstanceOf("defineStringConst")
                    .add("index", index)
                    .add("tag", Primitive.STRING.getTag())
                    .add("value", s)
                    .add("len", getIntLabel(s.length()))
                    .add("noWords", 5 + (s.length() + 1) / 4);

            constantsST.add("e", st);
        }
        return "str_const_" + index;
    }

    private StringBuilder extractArgs(ArrayList<Expression> argsList) {
        StringBuilder args = new StringBuilder();

        Collections.reverse(argsList);

        for (Expression arg : argsList) {
            ST st = templates.getInstanceOf("dispatchArg")
                    .add("arg", arg.accept(this));
            if (st != null) {
                args.append(st.render());
                args.append("\n");
            }
        }

        return args;
    }

    private String getFileName(ParserRuleContext ctx) {
        while (ctx != null && !(ctx.getParent() instanceof CoolParser.ProgramContext)) {
            ctx = ctx.getParent();
        }

        return getStringLabel(new File(Compiler.fileNames.get(ctx)).getName());
    }
}
