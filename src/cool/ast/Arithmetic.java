package cool.ast;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

// Constructia operatiilor aritmetice.
public class Arithmetic extends Expression {
    Expression leftOp;
    Expression rightOp;
    Token op;

    Arithmetic(ParserRuleContext ctx, Expression leftOp, Expression rightOp, Token op, Token start) {
        super(ctx, start);
        this.leftOp = leftOp;
        this.rightOp = rightOp;
        this.op = op;
    }

    public Expression getLeftOp() {
        return leftOp;
    }

    public Expression getRightOp() {
        return rightOp;
    }

    public Token getLeftToken() {
        return leftOp.token;
    }

    public Token getRightToken() {
        return rightOp.token;
    }

    public String getOp() {
        return op.getText();
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
