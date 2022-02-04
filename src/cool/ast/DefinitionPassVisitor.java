package cool.ast;

import cool.structures.*;
import org.antlr.v4.runtime.ParserRuleContext;

public class DefinitionPassVisitor implements ASTVisitor<Void> {
    private Scope currentScope = null;

    @Override
    public Void visit(Assign assign) {
        assign.getValue().accept(this);
        return null;
    }

    @Override
    public Void visit(StaticDispatch staticDispatch) {
        return null;
    }

    @Override
    public Void visit(Dispatch dispatch) {
        return null;
    }

    @Override
    public Void visit(If iff) {
        iff.getCond().accept(this);
        iff.getThenBranch().accept(this);
        iff.getElseBranch().accept(this);
        return null;
    }

    @Override
    public Void visit(While whilee) {
        whilee.getCond().accept(this);

        for (var e : whilee.getBody()) {
            e.accept(this);
        }
        return null;
    }

    @Override
    public Void visit(Block block) {
        for (var e : block.getBody()) {
            e.accept(this);
        }
        return null;
    }

    @Override
    public Void visit(Let let) {
        LetScope scope = new LetScope(currentScope);
        let.setScope(scope);

        currentScope = scope;

        for (var d : let.getVars()) {
            d.accept(this);
        }
        let.getBody().accept(this);

        currentScope = currentScope.getParent();

        return null;
    }

    @Override
    public Void visit(CaseDef caseDef) {
        ParserRuleContext ctx = caseDef.getContext();
        var name = caseDef.getId();
        var type = caseDef.getType();

        if (name.equals("self")) {
            SymbolTable.error(ctx, caseDef.getIdToken(), "Case variable has illegal name " + name);
            return null;
        }

        if (type.equals("SELF_TYPE")) {
            SymbolTable.error(ctx, caseDef.getTypeToken(), "Case variable " + name + " has illegal type " + type);
            return null;
        }
        caseDef.setValid();
        caseDef.getBody().accept(this);
        return null;
    }

    @Override
    public Void visit(Case casee) {
        casee.getValue().accept(this);
        for (var c : casee.getCases()) {
            c.accept(this);
        }
        return null;
    }

    @Override
    public Void visit(New neww) {
        return null;
    }

    @Override
    public Void visit(VoidCheck voidCheck) {
        voidCheck.getValue().accept(this);
        return null;
    }

    @Override
    public Void visit(Arithmetic arithmetic) {
        arithmetic.getLeftOp().accept(this);
        arithmetic.getRightOp().accept(this);
        return null;
    }

    @Override
    public Void visit(Neg neg) {
        neg.getValue().accept(this);
        return null;
    }

    @Override
    public Void visit(Relational relational) {
        relational.getLeftOp().accept(this);
        relational.getRightOp().accept(this);
        return null;
    }

    @Override
    public Void visit(Not not) {
        not.getValue().accept(this);
        return null;
    }

    @Override
    public Void visit(Id id) {
        return null;
    }

    @Override
    public Void visit(Int i) {
        return null;
    }

    @Override
    public Void visit(Stringg s) {
        return null;
    }

    @Override
    public Void visit(Bool b) {
        return null;
    }

    @Override
    public Void visit(Formal formal) {
        ParserRuleContext ctx = formal.getContext();
        MethodScope scope = (MethodScope) currentScope;
        String name = formal.getName();
        String type = formal.getType();
        String methodName = scope.getName();
        String className = scope.getParent().getName();

        if (formal.getName().equals("self")) {
            SymbolTable.error(ctx, formal.getNameToken(), "Method " + methodName + " of class "  + className +
                    " has formal parameter with illegal name " + name);
            return null;
        }

        if (type.equals("SELF_TYPE")) {
            SymbolTable.error(ctx, formal.getTypeToken(), "Method " + methodName + " of class "  + className +
                    " has formal parameter " + name + " with illegal type " + type);
            return null;
        }

        IdSymbol symbol = new IdSymbol(name, formal.getTypeToken());
        boolean r = scope.add(symbol);
        if (!r) {
            SymbolTable.error(ctx, formal.getNameToken(), "Method " + methodName + " of class " + className +
                    " redefines formal parameter " + name);
            return null;
        }

        formal.setIdSymbol(symbol);

        return null;
    }

    @Override
    public Void visit(VarDef varDef) {
        ParserRuleContext ctx = varDef.getContext();
        var name = varDef.getId();

        if (name.equals("self")) {
            SymbolTable.error(ctx, varDef.getIdToken(),"Let variable has illegal name " + name);
            return null;
        }

        var symbol = new IdSymbol(name, varDef.getTypeToken());
        varDef.setIdSymbol(symbol);

        if (varDef.hasInitValue()) {
            varDef.getValue().accept(this);
        }

        return null;
    }

    @Override
    public Void visit(ClassElement classElement) {
        ParserRuleContext ctx = classElement.getContext();
        TypeSymbol scope = (TypeSymbol) currentScope;
        String className = scope.getName();
        if (classElement.isMethod()) {
            // Metoda
            String name = classElement.getName();

            MethodScope methodScope = new MethodScope(name);
            methodScope.setParent(scope);

            if (!scope.addMethod(methodScope)) {
                SymbolTable.error(ctx, classElement.getIdToken(),
                        "Class " + className  + " redefines method " + name);
                return null;
            }

            classElement.setMethodSymbol(methodScope);

            currentScope = methodScope;

            for (var e: classElement.getParams()) {
                e.accept(this);
            }
            for (var e: classElement.getBody()) {
                e.accept(this);
            }

            currentScope = scope;

            int offset = 12;
            for (Formal f : classElement.getParams()) {
                f.getIdSymbol().setOffset(offset);
                offset += 4;
            }
        } else {
            // Atribut
            String name = classElement.getName();

            if (name.equals("self")) {
                SymbolTable.error(ctx, classElement.getIdToken(),
                        "Class " + className  + " has attribute with illegal name " + name);
                return null;
            }

            IdSymbol symbol = new IdSymbol(name, classElement.getTypeToken());
            var r = scope.add(symbol);
            if (!r) {
                SymbolTable.error(ctx, classElement.getIdToken(),
                        "Class " + className  + " redefines attribute " + name);
                return null;
            }

            classElement.setIdSymbol(symbol);

            if (classElement.hasInitValue()) {
                classElement.getValue().accept(this);
            }
        }
        return null;
    }

    @Override
    public Void visit(ClassDef classDef) {
        ParserRuleContext ctx = classDef.getContext();
        String name = classDef.getType();
        // Verific ca numele sa nu fie SELF_TYPE
        if (name.equals("SELF_TYPE")) {
            SymbolTable.error(ctx, classDef.getTypeToken(), "Class has illegal name SELF_TYPE");
            return null;
        }

        // Obtin numele clasei parinte (default este Object)
        String parentType = "Object";
        if (classDef.getParent() != null) {
            parentType = classDef.getParent().getText();
        }

        // Verific sa nu aiba parinti ilegali
        if (parentType.equals("Int") ||
                parentType.equals("String") ||
                parentType.equals("Bool") ||
                parentType.equals("SELF_TYPE")) {
            SymbolTable.error(ctx, classDef.getParentToken(), "Class " + name + " has illegal parent " +parentType);
        }


        TypeSymbol classScope = new TypeSymbol(name, parentType);
        classDef.setScope(classScope);
        IdSymbol self = new IdSymbol("self", null);
        self.setType(classScope);
        classScope.add(self);

        var r = SymbolTable.globalScope.add(classScope);

        if (!r) {
            SymbolTable.error(ctx, classDef.getTypeToken(), "Class " + name + " is redefined");
            return null;
        }

        currentScope = classScope;

        for (var e: classDef.getBody()) {
            this.visit(e);
        }

        return null;
    }

    @Override
    public Void visit(Prog prog) {
        currentScope = SymbolTable.globalScope;

        for (var c : prog.getClasses())
            c.accept(this);

        return null;
    }
}
