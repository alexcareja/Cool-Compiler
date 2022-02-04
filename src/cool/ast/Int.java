package cool.ast;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public class Int extends Expression {
    Token i;

    Int(ParserRuleContext ctx, Token i, Token start) {
        super(ctx, start);
        this.i = i;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
