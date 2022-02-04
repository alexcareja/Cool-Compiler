package cool.ast;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public class Bool extends Expression {
    Token b;

    Bool(ParserRuleContext ctx, Token b, Token start) {
        super(ctx, start);
        this.b = b;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
