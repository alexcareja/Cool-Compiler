package cool.ast;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public class New extends Expression {
    Token type;

    New(ParserRuleContext ctx, Token type, Token start) {
        super(ctx, start);
        this.type = type;
    }

    public String getType() {
        return type.getText();
    }

    public Token getTypeToken() {
        return type;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
