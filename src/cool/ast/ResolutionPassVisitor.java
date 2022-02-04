package cool.ast;

import cool.structures.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import java.util.ArrayList;

public class ResolutionPassVisitor implements ASTVisitor<TypeSymbol> {
    private Scope currentScope;

    private TypeSymbol lookup(String typeName) {
        if (typeName.equals("SELF_TYPE")) {
            if (currentScope instanceof TypeSymbol) {
                return (TypeSymbol) currentScope;
            }
            Scope scope = currentScope;
            while (scope != null) {
                scope = scope.getParent();
                if (scope instanceof TypeSymbol) {
                    return (TypeSymbol) scope;
                }
            }
            return null;
        }
        return (TypeSymbol) SymbolTable.globalScope.lookup(typeName);
    }

    private void checkDispatchArguments(ParserRuleContext ctx, Token idToken, String name, String className,
                                           ArrayList<Expression> args, MethodScope methodScope) {
        ArrayList<IdSymbol> formals = methodScope.getFormals();
        if (args.size() != formals.size()) {
            SymbolTable.error(ctx, idToken,
                    "Method " + name + " of class " + className + " is applied to wrong number of arguments");
            return ;
        }

        int i = 0;
        for (var e : args) {
            TypeSymbol argSymbol = e.accept(this);
            if (argSymbol == null) {
                i++;
                continue;
            }
            String argType = argSymbol.getName();
            String formalType = formals.get(i).getType().getName();
            TypeSymbol formalSymbol = (TypeSymbol) SymbolTable.globalScope.lookup(formalType);
            String id = formals.get(i).getName();
            if (!argType.equals(formalType) && argSymbol.doesNotRelate(formalSymbol)) {
                SymbolTable.error(ctx, e.token, "In call to method " + name + " of class " + className +
                        ", actual type " + argType + " of formal parameter " + id +
                        " is incompatible with declared type " + formalType);
                break;
            }
            i++;
        }
    }

    private void checkOperandsAreIntegers(ParserRuleContext ctx, TypeSymbol lo, TypeSymbol ro, Token loT, Token roT,
                                          String op) {
        if (ro != Primitive.INT) {
            SymbolTable.error(ctx, roT, "Operand of " + op + " has type " + ro.getName() + " instead of Int");
            return;
        }
        if (lo != Primitive.INT) {
            SymbolTable.error(ctx, loT, "Operand of " + op + " has type " + lo.getName() + " instead of Int");
        }
    }

    @Override
    public TypeSymbol visit(Assign assign) {
        ParserRuleContext ctx = assign.getContext();
        String name = assign.getId();
        Expression value = assign.getValue();

        if (name.equals("self")) {
            SymbolTable.error(ctx, assign.getIdToken(), "Cannot assign to self");
            return null;
        }


        IdSymbol idSymbol = (IdSymbol) currentScope.lookup(name);
        if (idSymbol == null) {
            return null;
        }
        TypeSymbol idType = idSymbol.getType();
        String type = idType.toString();

        TypeSymbol typeSymbol = lookup(idSymbol.getType().getName());
        if (typeSymbol == null) {
            return null;
        }

        TypeSymbol symbol = value.accept(this);
        if (symbol == null) {
            return null;
        }

        if (!type.equals(symbol.getName()) && symbol.doesNotRelate(typeSymbol)) {
            SymbolTable.error(ctx, assign.getToken(), "Type " + symbol +
                    " of assigned expression is incompatible with declared type " + type + " of identifier " + name);
            return null;
        }

        return idType;
    }

    @Override
    public TypeSymbol visit(StaticDispatch staticDispatch) {
        ParserRuleContext ctx = staticDispatch.getContext();
        String name = staticDispatch.getId();
        TypeSymbol invokerClass = staticDispatch.getInvoker().accept(this);
        if (invokerClass == null) {
            return Primitive.OBJECT;
        }
        TypeSymbol realInvokerClass = lookup(invokerClass.getName());
        if (realInvokerClass == null) {
            return Primitive.OBJECT;
        }
        String className = invokerClass.getName();
        String realClassName = realInvokerClass.getName();
        ArrayList<Expression> args = staticDispatch.getArgs();
        MethodScope methodScope;

        if (staticDispatch.hasType()) {
            // (expr)@(type).id(formals)
            className = staticDispatch.getType();

            if (className.equals("SELF_TYPE")) {
                SymbolTable.error(ctx, staticDispatch.getTypeToken(), "Type of static dispatch cannot be SELF_TYPE");
                return Primitive.OBJECT;
            }

            TypeSymbol classScope = lookup(className);
            if (classScope == null) {
                SymbolTable.error(ctx, staticDispatch.getTypeToken(),
                        "Type " + className + " of static dispatch is undefined");
                return Primitive.OBJECT;
            }
            className = classScope.getName();
            methodScope = classScope.lookupMethod(name);

            if (!realClassName.equals(classScope.getName()) && realInvokerClass.doesNotRelate(classScope)) {
                SymbolTable.error(ctx, staticDispatch.getTypeToken(), "Type " + className +
                        " of static dispatch is not a superclass of type " + realInvokerClass);
                return Primitive.OBJECT;
            }
            realClassName = classScope.getName();

            staticDispatch.setCallerType(classScope);
        } else {
            // (expr).id(formals)
            methodScope = realInvokerClass.lookupMethod(name);
            staticDispatch.setCallerType(realInvokerClass);
        }
        if (methodScope == null) {
            SymbolTable.error(ctx, staticDispatch.getIdToken(),
                    "Undefined method " + name + " in class " + realClassName);
            return Primitive.OBJECT;
        }
        checkDispatchArguments(ctx, staticDispatch.getIdToken(), name, realClassName, args, methodScope);

        if (methodScope.getReturnType().getName().equals("SELF_TYPE")) {
            return invokerClass;
        }

        return methodScope.getReturnType();
    }

    @Override
    public TypeSymbol visit(Dispatch dispatch) {
        ParserRuleContext ctx = dispatch.getContext();
        String name = dispatch.getId();
        TypeSymbol classScope = lookup("SELF_TYPE");
        if (classScope == null) {
            return Primitive.OBJECT;
        }
        dispatch.setCallerType(classScope);

        String className = classScope.getName();
        MethodScope symbol = classScope.lookupMethod(name);

        if (symbol == null) {
            SymbolTable.error(ctx, dispatch.getIdToken(), "Undefined method " + name + " in class " + className);
            return Primitive.OBJECT;
        }

        ArrayList<Expression> args = dispatch.getArgs();
        checkDispatchArguments(ctx, dispatch.getIdToken(), name, className, args, symbol);

        TypeSymbol returnType = symbol.getReturnType();

        if (returnType == Primitive.SELF_TYPE) {
            returnType = lookup("SELF_TYPE");
        }

        return returnType;
    }

    @Override
    public TypeSymbol visit(If iff) {
        ParserRuleContext ctx = iff.getContext();
        TypeSymbol condSym = iff.getCond().accept(this);

        if (condSym != Primitive.BOOL) {
            String condType = condSym.toString();
            SymbolTable.error(ctx, iff.getCondToken(), "If condition has type " + condType + " instead of Bool");
        }

        TypeSymbol thenSym = iff.getThenBranch().accept(this);
        TypeSymbol elseSym = iff.getElseBranch().accept(this);

        return thenSym.leastCommonAncestor(elseSym);
    }

    @Override
    public TypeSymbol visit(While whilee) {
        ParserRuleContext ctx = whilee.getContext();
        TypeSymbol condSym = whilee.getCond().accept(this);
        String condType = condSym.toString();

        if (condSym != Primitive.BOOL) {
            SymbolTable.error(ctx, whilee.getCondToken(), "While condition has type " + condType + " instead of Bool");
        }

        for (Expression e: whilee.body) {
            e.accept(this);
        }

        return Primitive.OBJECT;
    }

    @Override
    public TypeSymbol visit(Block block) {
        TypeSymbol sym = null;

        for (var e : block.getBody()) {
            sym = e.accept(this);
        }

        return sym;
    }

    @Override
    public TypeSymbol visit(Let let) {
        currentScope = let.getScope();
        IdSymbol sym;

        for (var v : let.getVars()) {
            v.accept(this);

            sym = v.getIdSymbol();
            if (sym != null) {
                currentScope.add(sym);
            }
        }

        TypeSymbol bodyType = let.getBody().accept(this);
        currentScope = currentScope.getParent();

        return bodyType;
    }

    @Override
    public TypeSymbol visit(CaseDef caseDef) {
        ParserRuleContext ctx = caseDef.getContext();
        String type = caseDef.getType();
        String id = caseDef.getId();

        if (type.equals("SELF_TYPE") || !caseDef.isValid()) {
            return null;
        }

        TypeSymbol caseType = (TypeSymbol) SymbolTable.globalScope.lookup(type);
        if (caseType == null) {
            SymbolTable.error(ctx, caseDef.getTypeToken(), "Case variable " + id + " has undefined type " + type);
            return null;
        }
        caseDef.setTypeSymbol(caseType);

        DefaultScope caseScope = new DefaultScope(currentScope);

        caseDef.setScope(caseScope);

        IdSymbol idSym = new IdSymbol(id, caseDef.getTypeToken());

        idSym.setType(caseType);
        idSym.setOffset(-4);
        caseScope.add(idSym);

        currentScope = caseScope;

        TypeSymbol bodyType = caseDef.getBody().accept(this);

        currentScope = caseScope.getParent();

        return bodyType;
    }

    @Override
    public TypeSymbol visit(Case casee) {
        TypeSymbol sym = null;

        for (var c : casee.getCases()) {
            TypeSymbol s = c.accept(this);
            if (sym == null) {
                sym = s;
            } else {
                sym = sym.leastCommonAncestor(s);
            }
        }

        return sym;
    }

    @Override
    public TypeSymbol visit(New neww) {
        ParserRuleContext ctx = neww.getContext();
        String type = neww.getType();

        TypeSymbol classScope = lookup(type);
        if (classScope == null) {
            SymbolTable.error(ctx, neww.getTypeToken(), "new is used with undefined type " + type);
            return null;
        }

        return (TypeSymbol) SymbolTable.globalScope.lookup(type);
    }

    @Override
    public TypeSymbol visit(VoidCheck voidCheck) {
        return Primitive.BOOL;
    }

    @Override
    public TypeSymbol visit(Arithmetic arithmetic) {
        ParserRuleContext ctx = arithmetic.getContext();
        TypeSymbol leftOpSym = arithmetic.getLeftOp().accept(this);
        TypeSymbol rightOpSym = arithmetic.getRightOp().accept(this);

        if (leftOpSym == null || rightOpSym == null) {
            return Primitive.INT;
        }

        String op = arithmetic.getOp();

        checkOperandsAreIntegers(ctx, leftOpSym, rightOpSym, arithmetic.getLeftToken(), arithmetic.getRightToken(), op);

        return Primitive.INT;
    }

    @Override
    public TypeSymbol visit(Neg neg) {
        ParserRuleContext ctx = neg.getContext();
        TypeSymbol symbol = neg.getValue().accept(this);
        String type = symbol.getName();
        if (symbol != Primitive.INT) {
            SymbolTable.error(ctx, neg.getToken(), "Operand of ~ has type " + type + " instead of Int");
            return Primitive.BOOL;
        }
        return symbol;
    }

    @Override
    public TypeSymbol visit(Relational relational) {
        ParserRuleContext ctx = relational.getContext();
        TypeSymbol leftOpSym = relational.getLeftOp().accept(this);
        TypeSymbol rightOpSym = relational.getRightOp().accept(this);

        if (leftOpSym == null || rightOpSym == null) {
            return Primitive.BOOL;
        }

        String op = relational.getOp();
        String leftOpType = leftOpSym.getName();
        String rightOpType = rightOpSym.getName();

        if (op.equals("=")) {
            if ((leftOpSym == Primitive.BOOL && rightOpSym != Primitive.BOOL) ||
                    (leftOpSym == Primitive.INT && rightOpSym != Primitive.INT) ||
                    (leftOpSym == Primitive.STRING && rightOpSym != Primitive.STRING)) {
                SymbolTable.error(ctx, relational.getOpToken(),
                        "Cannot compare " + leftOpType + " with " + rightOpType);
            }
        } else {
            checkOperandsAreIntegers(ctx, leftOpSym, rightOpSym, relational.getLeftToken(), relational.getRightToken(),
                    op);
        }

        return Primitive.BOOL;
    }

    @Override
    public TypeSymbol visit(Not not) {
        ParserRuleContext ctx = not.getContext();
        TypeSymbol symbol = not.getValue().accept(this);
        if (symbol == null) {
            return Primitive.BOOL;
        }
        String type = symbol.getName();

        if (symbol != Primitive.BOOL) {
            SymbolTable.error(ctx, not.getToken(), "Operand of not has type " + type + " instead of Bool");
            return Primitive.BOOL;
        }
        return Primitive.BOOL;
    }

    @Override
    public TypeSymbol visit(Id id) {
        ParserRuleContext ctx = id.getContext();
        String name = id.getId();
        IdSymbol idSymbol = (IdSymbol) currentScope.lookup(name);
        if (idSymbol == null) {
            SymbolTable.error(ctx, id.getIdToken(), "Undefined identifier " + name);
            return null;
        }
        return idSymbol.getType();
    }

    @Override
    public TypeSymbol visit(Int i) {
        return Primitive.INT;
    }

    @Override
    public TypeSymbol visit(Stringg s) {
        return Primitive.STRING;
    }

    @Override
    public TypeSymbol visit(Bool b) {
        return Primitive.BOOL;
    }

    @Override
    public TypeSymbol visit(Formal formal) {
        return null;
    }

    @Override
    public TypeSymbol visit(VarDef varDef) {
        ParserRuleContext ctx = varDef.getContext();
        String name = varDef.getId();
        String type = varDef.getType();
        IdSymbol idSymbol = varDef.getIdSymbol();

        if (idSymbol == null) {
            return null;
        }

        TypeSymbol varSym = lookup(type);
        if (varSym == null) {
            SymbolTable.error(ctx, varDef.getTypeToken(), "Let variable " + name + " has undefined type " + type);
            varDef.setIdSymbol(null);

            return null;
        }

        Expression value = varDef.getValue();
        if (value != null) {
            TypeSymbol valueType = value.accept(this);
            if (valueType != null && valueType != varSym  && valueType.doesNotRelate(varSym)) {
                SymbolTable.error(ctx, varDef.getValueToken(), "Type " + valueType +
                        " of initialization expression " + "of identifier " + name +
                        " is incompatible with declared type " + type);
                return null;
            }
        }

        TypeSymbol symbol = (TypeSymbol) SymbolTable.globalScope.lookup(type);
        idSymbol.setType(symbol);

        return null;
    }

    @Override
    public TypeSymbol visit(ClassElement classElement) {
        ParserRuleContext ctx = classElement.getContext();
        if (classElement.isMethod()) {
            // Metoda
            String name = classElement.getName();
            String className = ((TypeSymbol) currentScope).getName();
            TypeSymbol scope = (TypeSymbol) currentScope;
            TypeSymbol parentClassScope = (TypeSymbol) scope.getParent();
            MethodScope methodScope = classElement.getMethodScope();
            if (methodScope == null) {
                return null;
            }
            String declaredReturnType = methodScope.getReturnType().getName();
            TypeSymbol returnType = lookup(declaredReturnType);

            if (returnType == null) {
                return null;
            }

            if (parentClassScope == null) {
                return null;
            }
            MethodScope oldMethodScope = parentClassScope.lookupMethod(name);
            if (oldMethodScope != null) {
                // Metoda este suprascrisa
                ArrayList<IdSymbol> methodFormals = classElement.getMethodScope().getFormals();
                ArrayList<IdSymbol> oldMethodFormals = oldMethodScope.getFormals();

                methodScope.setOldMethodScope(oldMethodScope);

                if (methodFormals.size() != oldMethodFormals.size()) {
                    SymbolTable.error(ctx, classElement.getIdToken(), "Class " + className + " overrides method " +
                            name + " with different number of formal parameters");
                    return null;
                }

                int i = 0;
                for (var f : methodFormals) {
                    if (f.getType() != oldMethodFormals.get(i).getType()) {
                        String formalId = f.getName();
                        String methodFormalType = f.getType().getName();
                        String oldMethodFormalType = oldMethodFormals.get(i).getType().getName();
                        SymbolTable.error(ctx, f.getTypeToken(), "Class " + className +
                                " overrides method " + name + " but changes type of formal parameter " + formalId +
                                " from " + oldMethodFormalType + " to " + methodFormalType);
                        return null;
                    }
                    i++;
                }

                TypeSymbol oldReturnType = oldMethodScope.getReturnType();
                if (!oldReturnType.getName().equals(declaredReturnType)) {
                    SymbolTable.error(ctx, classElement.getTypeToken(), "Class " + className +
                            " overrides method " + name + " but changes return type from " + oldReturnType + " to " +
                            returnType);
                    return null;
                }
            }

            TypeSymbol symbol = returnType;

            currentScope = methodScope;
            for (var e : classElement.getBody()) {
                symbol = e.accept(this);
            }

            currentScope = scope;

            if (symbol == null) {
                return null;
            }

            String bodyType = symbol.getName();

            if (bodyType.equals("SELF_TYPE")) {
                TypeSymbol sym = lookup("SELF_TYPE");
                if (sym == null) {
                    return null;
                }
                if (!bodyType.equals(declaredReturnType) && !declaredReturnType.equals(sym.getName())
                        && sym.doesNotRelate(returnType)) {
                    SymbolTable.error(ctx, classElement.getBodyTypeToken(),
                            "Type " + bodyType + " of the body of method " + name +
                                    " is incompatible with declared return type " + declaredReturnType);
                }
                return null;
            }

            if (symbol.getName().equals("SELF_TYPE")) {
                TypeSymbol sym = lookup("SELF_TYPE");
                if (sym == null) {
                    return null;
                }
                if (!bodyType.equals(declaredReturnType) && symbol.doesNotRelate(returnType) &&
                        sym.doesNotRelate(returnType)) {
                    SymbolTable.error(ctx, classElement.getBodyTypeToken(),
                            "Type " + bodyType + " of the body of method " + name +
                                    " is incompatible with declared return type " + declaredReturnType);
                }
                return null;
            }

            if (!bodyType.equals(declaredReturnType) && symbol.doesNotRelate(returnType)) {
                SymbolTable.error(ctx, classElement.getBodyTypeToken(),
                        "Type " + bodyType + " of the body of method " + name +
                                " is incompatible with declared return type " + declaredReturnType);
            }

            return null;
        } else {
            // Atribut
            String id = classElement.getName();
            String declaredType = classElement.getType();
            TypeSymbol symbol = lookup(declaredType);
            IdSymbol idSymbol = (IdSymbol) currentScope.lookup(id);
            if (symbol == null) {
                return null;
            }

            if (classElement.hasInitValue()) {
                TypeSymbol valueSym = classElement.getValue().accept(this);
                if (valueSym == null) {
                    return null;
                }
                String valueType = valueSym.getName();
                valueSym = lookup(valueType);

                if (valueSym != null) {
                    if (valueSym != symbol && valueSym.doesNotRelate(symbol)) {
                        SymbolTable.error(ctx, classElement.getValueToken(), "Type " + valueType +
                                " of initialization expression of attribute " + id +
                                " is incompatible with declared type " + declaredType);
                        return null;
                    }
                }
            }

            if (!declaredType.equals("SELF_TYPE")) {
                idSymbol.setType(symbol);
            }

            return symbol;
        }
    }

    @Override
    public TypeSymbol visit(ClassDef classDef) {
        currentScope = classDef.getScope();

        for (var e : classDef.getBody()) {
            e.accept(this);
        }

        int methodOffset = classDef.getScope().countParentMethods() * 4;
        int attributeOffset = 12;
        TypeSymbol parent = (TypeSymbol) classDef.getScope().getParent();

        if (parent != null) {
            attributeOffset += parent.getAttributesSize();
        }

        for (ClassElement ce : classDef.getBody()) {
            if (ce.isMethod()) {
                MethodScope methodScope = ce.getMethodScope();
                if (methodScope.getOldMethodScope() == null) {
                    methodScope.setOffset(methodOffset);
                    methodOffset += 4;
                }
            } else {
                ce.getIdSymbol().setAsAttribute(true);
                ce.getIdSymbol().setOffset(attributeOffset);
                attributeOffset += 4;
            }
        }

        return null;
    }

    @Override
    public TypeSymbol visit(Prog prog) {
        for (var c : prog.getClasses()) {
            c.accept(this);
        }
        return null;
    }
}
