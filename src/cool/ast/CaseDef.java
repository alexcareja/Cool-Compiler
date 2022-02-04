package cool.ast;

import cool.structures.DefaultScope;
import cool.structures.TypeSymbol;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public class CaseDef extends ASTNode {
    Token token;
    Token id;
    Token type;
    Expression body;
    ParserRuleContext ctx;
    boolean valid = false;
    TypeSymbol typeSymbol;
    DefaultScope scope;

    CaseDef(ParserRuleContext ctx, Token id, Token type, Expression body, Token token) {
        this.ctx = ctx;
        this.token = token;
        this.id = id;
        this.type = type;
        this.body = body;
    }

    public Token getToken() {
        return token;
    }

    public ParserRuleContext getContext() {
        return ctx;
    }

    public String getId() {
        return id.getText();
    }

    public Token getIdToken() {
        return id;
    }

    public String getType() {
        return type.getText();
    }

    public Token getTypeToken() {
        return type;
    }

    public Expression getBody() {
        return body;
    }

    public TypeSymbol getTypeSymbol() {
        return typeSymbol;
    }

    public DefaultScope getScope() {
        return scope;
    }

    public void setValid() {
        this.valid = true;
    }

    public void setTypeSymbol(TypeSymbol typeSymbol) {
        this.typeSymbol = typeSymbol;
    }

    public void setScope(DefaultScope scope) {
        this.scope = scope;
    }

    public boolean isValid() {
        return valid;
    }


    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
