package cool.ast;

import cool.structures.*;
import org.antlr.v4.runtime.ParserRuleContext;

public class HierarchyVisitor implements ASTVisitor<Void> {
    private Scope currentScope;

    @Override
    public Void visit(Assign assign) {
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
        return null;
    }

    @Override
    public Void visit(While whilee) {
        return null;
    }

    @Override
    public Void visit(Block block) {
        return null;
    }

    @Override
    public Void visit(Let let) {
        return null;
    }

    @Override
    public Void visit(CaseDef caseDef) {
        return null;
    }

    @Override
    public Void visit(Case casee) {
        return null;
    }

    @Override
    public Void visit(New neww) {
        return null;
    }

    @Override
    public Void visit(VoidCheck voidCheck) {
        return null;
    }

    @Override
    public Void visit(Arithmetic arithmetic) {
        return null;
    }

    @Override
    public Void visit(Neg neg) {
        return null;
    }

    @Override
    public Void visit(Relational relational) {
        return null;
    }

    @Override
    public Void visit(Not not) {
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
        String name = formal.getName();
        String type = formal.getType();
        String methodName = ((MethodScope) currentScope).getName();
        String className = ((TypeSymbol) currentScope.getParent()).getName();
        IdSymbol formalSym = formal.getIdSymbol();

        if (formalSym == null) {
            return null;
        }

        TypeSymbol formalType= (TypeSymbol) SymbolTable.globalScope.lookup(type);
        if (formalType == null) {
            SymbolTable.error(ctx, formal.getTypeToken(), "Method " + methodName + " of class " + className +
                    " has formal parameter " + name + " with undefined type " + type);
        } else {
            formalSym.setType(formalType);
        }

        return null;
    }

    @Override
    public Void visit(VarDef vardef) {
        return null;
    }

    @Override
    public Void visit(ClassElement classElement) {
        ParserRuleContext ctx = classElement.getContext();
        String name = classElement.getName();
        String type = classElement.getType();
        String className = ((TypeSymbol) currentScope).getName();

        if (classElement.isMethod()) {
            // Metoda
            TypeSymbol returnType = (TypeSymbol) SymbolTable.globalScope.lookup(type);
            MethodScope methodScope = classElement.getMethodScope();

            if (methodScope == null) {
                return null;
            }

            if (returnType == null) {
                SymbolTable.error(ctx, classElement.getTypeToken(),
                        "Class " + className + " has method " + name + " with undefined return type " + type);
                return null;
            }

            methodScope.setReturnType(returnType);

            currentScope = methodScope;

            for (var f : classElement.getParams()) {
                f.accept(this);
            }

            for (var e : classElement.getBody()) {
                e.accept(this);
            }

            currentScope = methodScope.getParent();
        } else {
            // Atribut
            TypeSymbol parentScope = (TypeSymbol) currentScope.getParent();
            IdSymbol oldAttribute = (IdSymbol) parentScope.lookup(name);
            IdSymbol attribute = classElement.getIdSymbol();

            if (attribute == null) {
                return null;
            }

            if (oldAttribute != null) {
                SymbolTable.error(ctx, classElement.getIdToken(),
                        "Class " + className + " redefines inherited attribute " + name);
                classElement.setIdSymbol(null);
                return null;
            }

            TypeSymbol attributeType = (TypeSymbol) SymbolTable.globalScope.lookup(type);
            if (attributeType == null) {
                SymbolTable.error(ctx, classElement.getTypeToken(),
                        "Class " + className + " has attribute " + name + " with undefined type " + type);
                classElement.setIdSymbol(null);
                return null;
            }

            attribute.setType(attributeType);
        }
        return null;
    }

    @Override
    public Void visit(ClassDef classDef) {
        ParserRuleContext ctx = classDef.getContext();
        TypeSymbol scope = classDef.getScope();
        if (scope == null) {
            return null;
        }
        String name = classDef.getType();
        String parent = scope.getParentName();
        TypeSymbol parentScope = (TypeSymbol) SymbolTable.globalScope.lookup(parent);

        if (!parent.equals("Object")) {
            if (parentScope == null) {
                SymbolTable.error(ctx, classDef.getParentToken(),
                        "Class " + name + " has undefined parent " + parent);
                return null;
            }
        }
        scope.setParent(parentScope);

        scope = (TypeSymbol) scope.getParent();
        String parentName;
        while (scope != Primitive.OBJECT && scope != null) {
            parentName = scope.getParentName();
            if (parentName == null) {
                break;
            }
            if (parentName.equals(name)) {
                SymbolTable.error(ctx, classDef.getTypeToken(), "Inheritance cycle for class " + name);
                return null;
            }
            scope = (TypeSymbol) SymbolTable.globalScope.lookup(parentName);
        }

        currentScope = classDef.getScope();

        for (var e : classDef.getBody()) {
            e.accept(this);
        }
        return null;
    }

    @Override
    public Void visit(Prog prog) {
        for (var c : prog.getClasses()) {
            c.accept(this);
        }
        return null;
    }
}
