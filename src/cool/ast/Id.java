package cool.ast;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public class Id extends Expression {
    Token id;

    Id(ParserRuleContext ctx, Token id, Token start) {
        super(ctx, start);
        this.id = id;
    }

    public String getId() {
        return id.getText();
    }

    public Token getIdToken() {
        return id;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
