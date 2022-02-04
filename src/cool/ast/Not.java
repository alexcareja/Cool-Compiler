package cool.ast;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public class Not extends Expression {
    Expression value;

    Not(ParserRuleContext ctx, Expression value, Token start) {
        super(ctx, start);
        this.value = value;
    }

    public Expression getValue() {
        return value;
    }

    public Token getToken() {
        return value.token;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
