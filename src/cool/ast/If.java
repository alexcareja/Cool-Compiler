package cool.ast;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public class If extends Expression {
    Expression cond;
    Expression thenBranch;
    Expression elseBranch;

    If(ParserRuleContext ctx, Expression cond, Expression thenBranch, Expression elseBranch, Token start) {
        super(ctx, start);
        this.cond = cond;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    public Expression getCond() {
        return cond;
    }

    public Token getCondToken() {
        return cond.token;
    }

    public Expression getThenBranch() {
        return thenBranch;
    }

    public Expression getElseBranch() {
        return elseBranch;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
