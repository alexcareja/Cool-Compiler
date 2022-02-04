package cool.ast;

public class PrintVisitor implements ASTVisitor<Void>{
    private int indent = 0;

    @Override
    public Void visit(Assign assign) {
        printIndent("<-");
        indent++;
        printIndent(assign.id.getText());
        assign.value.accept(this);
        indent--;
        return null;
    }

    @Override
    public Void visit(StaticDispatch staticDispatch) {
        printIndent(".");
        indent++;
        staticDispatch.invoker.accept(this);
        if (staticDispatch.type != null) {
            printIndent(staticDispatch.type.getText());
        }
        printIndent(staticDispatch.id.getText());
        for (var arg : staticDispatch.args) {
            arg.accept(this);
        }
        indent--;
        return null;
    }

    @Override
    public Void visit(Dispatch dispatch) {
        printIndent("implicit dispatch");
        indent++;
        printIndent(dispatch.id.getText());
        for (var arg : dispatch.args) {
            arg.accept(this);
        }
        indent--;
        return null;
    }

    @Override
    public Void visit(If iff) {
        printIndent("if");
        indent++;
        iff.cond.accept(this);
        iff.thenBranch.accept(this);
        iff.elseBranch.accept(this);
        indent--;
        return null;
    }

    @Override
    public Void visit(While whilee) {
        printIndent("while");
        indent++;
        whilee.cond.accept(this);
        for (var e : whilee.body) {
            e.accept(this);
        }
        indent--;
        return null;
    }

    @Override
    public Void visit(Block block) {
        printIndent("block");
        indent++;
        for (var e : block.body) {
            e.accept(this);
        }
        indent--;
        return null;
    }

    @Override
    public Void visit(Let let) {
        printIndent("let");
        indent++;
        for (var v : let.vars) {
            v.accept(this);
        }
        let.body.accept(this);
        indent--;
        return null;
    }

    @Override
    public Void visit(CaseDef caseDef) {
        printIndent("case branch");
        indent++;
        printIndent(caseDef.id.getText());
        printIndent(caseDef.type.getText());
        caseDef.body.accept(this);
        indent--;
        return null;
    }

    @Override
    public Void visit(Case casee) {
        printIndent("case");
        indent++;
        casee.value.accept(this);
        for (var c : casee.cases) {
            c.accept(this);
        }
        indent--;
        return null;
    }

    @Override
    public Void visit(New neww) {
        printIndent("new");
        indent++;
        printIndent(neww.type.getText());
        indent--;
        return null;
    }

    @Override
    public Void visit(VoidCheck voidCheck) {
        printIndent("isvoid");
        indent++;
        voidCheck.value.accept(this);
        indent--;
        return null;
    }

    @Override
    public Void visit(Arithmetic arithmetic) {
        printIndent(arithmetic.op.getText());
        indent++;
        arithmetic.leftOp.accept(this);
        arithmetic.rightOp.accept(this);
        indent--;
        return null;
    }

    @Override
    public Void visit(Neg neg) {
        printIndent("~");
        indent++;
        neg.value.accept(this);
        indent--;
        return null;
    }

    @Override
    public Void visit(Relational relational) {
        printIndent(relational.op.getText());
        indent++;
        relational.leftOp.accept(this);
        relational.rightOp.accept(this);
        indent--;
        return null;
    }

    @Override
    public Void visit(Not not) {
        printIndent("not");
        indent++;
        not.value.accept(this);
        indent--;
        return null;
    }

    @Override
    public Void visit(Id id) {
        printIndent(id.id.getText());
        return null;
    }

    @Override
    public Void visit(Int i) {
        printIndent(i.i.getText());
        return null;
    }

    @Override
    public Void visit(Stringg s) {
        printIndent(s.s.getText());
        return null;
    }

    @Override
    public Void visit(Bool b) {
        printIndent(b.b.getText());
        return null;
    }

    @Override
    public Void visit(Formal formal) {
        printIndent("formal");
        indent++;
        printIndent(formal.name.getText());
        printIndent(formal.type.getText());
        indent--;
        return null;
    }

    @Override
    public Void visit(VarDef vardef) {
        printIndent("local");
        indent++;
        printIndent(vardef.id.getText());
        printIndent(vardef.type.getText());
        if (vardef.value != null) {
            vardef.value.accept(this);
        }
        indent--;
        return null;
    }

    @Override
    public Void visit(ClassElement classElement) {
        if (classElement.body == null) {
            // Attribute
            printIndent("attribute");
            indent++;
            printIndent(classElement.id.getText());
            printIndent(classElement.type.getText());
            if (classElement.value != null) {
                classElement.value.accept(this);
            }
            indent--;
        } else {
            // Method
            printIndent("method");
            indent++;
            printIndent(classElement.id.getText());
            if (classElement.params != null) {
                // Formals
                for (var p : classElement.params) {
                    p.accept(this);
                }
            }
            printIndent(classElement.type.getText());
            for (var b : classElement.body) {
                b.accept(this);
            }
            indent--;
        }
        return null;
    }

    @Override
    public Void visit(ClassDef classDef) {
        printIndent("class");
        indent++;
        printIndent(classDef.type.getText());
        if (classDef.parent != null) {
            printIndent(classDef.parent.getText());
        }
        for (var el : classDef.body) {
            el.accept(this);
        }
        indent--;
        return null;
    }

    @Override
    public Void visit(Prog prog) {
        printIndent("program");
        indent++;
        for (var c : prog.classes) {
            c.accept(this);
        }
        indent--;
        return null;
    }

    void printIndent(String str) {
        for (int i = 0; i < 2 * indent; i++)
            System.out.print(" ");
        System.out.println(str);
    }
}
