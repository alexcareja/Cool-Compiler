package cool.ast;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public class Assign extends Expression {
    Token id;
    Expression value;

    Assign(ParserRuleContext ctx, Token id, Expression value, Token start) {
        super(ctx, start);
        this.id = id;
        this.value = value;
    }

    public String getId() {
        return id.getText();
    }

    public Expression getValue() {
        return value;
    }

    public Token getIdToken() {
        return id;
    }

    @Override
    public Token getToken() {
        return value.token;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
