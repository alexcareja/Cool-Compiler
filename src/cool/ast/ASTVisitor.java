package cool.ast;

public interface ASTVisitor<T> {
    T visit(Assign assign);
    T visit(StaticDispatch staticDispatch);
    T visit(Dispatch dispatch);
    T visit(If iff);
    T visit(While whilee);
    T visit(Block block);
    T visit(Let let);
    T visit(CaseDef caseDef);
    T visit(Case casee);
    T visit(New neww);
    T visit(VoidCheck voidCheck);
    T visit(Arithmetic arithmetic);
    T visit(Neg neg);
    T visit(Relational relational);
    T visit(Not not);
    T visit(Id id);
    T visit(Int i);
    T visit(Stringg s);
    T visit(Bool b);
    T visit(Formal formal);
    T visit(VarDef vardef);
    T visit(ClassElement classElement);
    T visit(ClassDef classDef);
    T visit(Prog prog);
}

