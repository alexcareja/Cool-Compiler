package cool.ast;

import cool.parser.CoolParser;
import cool.parser.CoolParserBaseVisitor;

import java.util.ArrayList;

public class ASTConstructionVisitor extends CoolParserBaseVisitor<ASTNode> {
    @Override public ASTNode visitProgram(CoolParser.ProgramContext ctx) {
        ArrayList<ClassDef> args = new ArrayList<>();
        for (var e : ctx.classes) {
            args.add((ClassDef) visit(e));
        }
        return new Prog(ctx, args, ctx.start);
    }

    @Override public ASTNode visitFormal(CoolParser.FormalContext ctx) {
        return new Formal(ctx, ctx.ID().getSymbol(), ctx.TYPE().getSymbol(), ctx.start);
    }

    @Override public ASTNode visitVarDef(CoolParser.VarDefContext ctx) {
        return ctx.value == null ?
                new VarDef(ctx, ctx.ID().getSymbol(), ctx.TYPE().getSymbol(), ctx.start)
                : new VarDef(ctx, ctx.ID().getSymbol(), ctx.TYPE().getSymbol(), (Expression) visit(ctx.value), ctx.start);
    }

    @Override public ASTNode visitAttribute(CoolParser.AttributeContext ctx) {
        return ctx.initValue == null ?
                new ClassElement(ctx, ctx.ID().getSymbol(), ctx.TYPE().getSymbol(), ctx.start)
                : new ClassElement(ctx, ctx.ID().getSymbol(), ctx.TYPE().getSymbol(),
                    (Expression) visit(ctx.initValue), ctx.start);
    }

    @Override public ASTNode visitMethod(CoolParser.MethodContext ctx) {
        ArrayList<Formal> formals = new ArrayList<>();
        ArrayList<Expression> body = new ArrayList<>();
        for (var f : ctx.formals) {
            formals.add((Formal) visit(f));
        }
        for (var e : ctx.body) {
            body.add((Expression) visit(e));
        }
        return new ClassElement(ctx, ctx.ID().getSymbol(), formals, ctx.TYPE().getSymbol(), body, ctx.start);
    }

    @Override public ASTNode visitClassDef(CoolParser.ClassDefContext ctx) {
        ArrayList<ClassElement> body = new ArrayList<>();
        for (var ce : ctx.classBody) {
            body.add((ClassElement) visit(ce));
        }
        return ctx.parent == null ?
                new ClassDef(ctx, ctx.TYPE(0).getSymbol(), body, ctx.start)
                : new ClassDef(ctx, ctx.TYPE(0).getSymbol(), ctx.parent, body, ctx.start);
    }

    @Override public ASTNode visitCaseDef(CoolParser.CaseDefContext ctx) {
        return new CaseDef(ctx, ctx.ID().getSymbol(), ctx.TYPE().getSymbol(), (Expression) visit(ctx.body), ctx.start);
    }

    @Override public ASTNode visitPar(CoolParser.ParContext ctx) {
        return ctx.par.accept(this);
    }

    @Override public ASTNode visitArithmetic(CoolParser.ArithmeticContext ctx) {
        return new Arithmetic(ctx, (Expression) visit(ctx.lo), (Expression) visit(ctx.ro), ctx.op, ctx.start);
    }

    @Override public ASTNode visitNew(CoolParser.NewContext ctx) {
        return new New(ctx, ctx.TYPE().getSymbol(), ctx.start);
    }

    @Override public ASTNode visitDispatch(CoolParser.DispatchContext ctx) {
        ArrayList<Expression> args = new ArrayList<>();
        for (var a : ctx.args) {
            args.add((Expression) visit(a));
        }
        return new Dispatch(ctx, ctx.ID().getSymbol(), args, ctx.start);
    }

    @Override public ASTNode visitString(CoolParser.StringContext ctx) {
        return new Stringg(ctx, ctx.STRING().getSymbol(), ctx.start);
    }

    @Override public ASTNode visitBool(CoolParser.BoolContext ctx) {
        return new Bool(ctx, ctx.BOOL().getSymbol(), ctx.start);
    }

    @Override public ASTNode visitRelational(CoolParser.RelationalContext ctx) {
        return new Relational(ctx, (Expression) visit(ctx.lo), (Expression) visit(ctx.ro), ctx.op, ctx.start);
    }

    @Override public ASTNode visitWhile(CoolParser.WhileContext ctx) {
        ArrayList<Expression> body = new ArrayList<>();
        for (var e : ctx.body) {
            body.add((Expression) visit(e));
        }
        return new While(ctx, (Expression) visit(ctx.cond), body, ctx.start);
    }

    @Override public ASTNode visitInt(CoolParser.IntContext ctx) {
        return new Int(ctx, ctx.INT().getSymbol(), ctx.start);
    }

    @Override public ASTNode visitNeg(CoolParser.NegContext ctx) {
        return new Neg(ctx, (Expression) visit(ctx.value), ctx.start);
    }

    @Override public ASTNode visitNot(CoolParser.NotContext ctx) {
        return new Not(ctx, (Expression) visit(ctx.value), ctx.start);
    }

    @Override public ASTNode visitStaticDispatch(CoolParser.StaticDispatchContext ctx) {
        ArrayList<Expression> args = new ArrayList<>();
        for (var a : ctx.args) {
            args.add((Expression) visit(a));
        }

        return ctx.TYPE() == null ? new StaticDispatch(ctx, (Expression) visit(ctx.invoker),
                    ctx.ID().getSymbol(), args, ctx.start)
                : new StaticDispatch(ctx, (Expression) visit(ctx.invoker), ctx.TYPE().getSymbol(),
                    ctx.ID().getSymbol(), args, ctx.start);
    }

    @Override public ASTNode visitVoidCheck(CoolParser.VoidCheckContext ctx) {
        return new VoidCheck(ctx, (Expression) visit(ctx.value), ctx.start);
    }

    @Override public ASTNode visitBlock(CoolParser.BlockContext ctx) {
        ArrayList<Expression> body = new ArrayList<>();
        for (var b : ctx.blockBody) {
            body.add((Expression) visit(b));
        }
        return new Block(ctx, body, ctx.start);
    }

    @Override public ASTNode visitLet(CoolParser.LetContext ctx) {
        ArrayList<VarDef> vars = new ArrayList<>();
        for (var v : ctx.vars) {
            vars.add((VarDef) visit(v));
        }
        return new Let(ctx, vars, (Expression) visit(ctx.body), ctx.start);
    }

    @Override public ASTNode visitId(CoolParser.IdContext ctx) {
        return new Id(ctx, ctx.ID().getSymbol(), ctx.start);
    }

    @Override public ASTNode visitIf(CoolParser.IfContext ctx) {
        return new If(ctx, (Expression) visit(ctx.cond), (Expression) visit(ctx.thenBranch),
                (Expression) visit(ctx.elseBranch), ctx.start);
    }

    @Override public ASTNode visitCase(CoolParser.CaseContext ctx) {
        ArrayList<CaseDef> cases = new ArrayList<>();
        for (var c : ctx.cases) {
            cases.add((CaseDef) visit(c));
        }
        return new Case(ctx, (Expression) visit(ctx.value), cases, ctx.start);
    }

    @Override public ASTNode visitAssign(CoolParser.AssignContext ctx) {
        return new Assign(ctx, ctx.ID().getSymbol(), (Expression) visit(ctx.value), ctx.start);
    }
}
