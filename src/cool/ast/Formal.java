package cool.ast;

import cool.structures.IdSymbol;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

// Definirea parametrilor functiilor.
public class Formal extends ASTNode {
    Token token;
    Token type;
    Token name;
    IdSymbol idSymbol;
    ParserRuleContext ctx;

    Formal(ParserRuleContext ctx, Token name, Token type, Token token) {
        this.ctx = ctx;
        this.token = token;
        this.name = name;
        this.type = type;
    }

    public ParserRuleContext getContext() {
        return ctx;
    }

    public Token getToken() {
        return token;
    }

    public String getName() {
        return name.getText();
    }

    public Token getNameToken() {
        return name;
    }

    public String getType() {
        return type.getText();
    }

    public Token getTypeToken() {
        return type;
    }

    public IdSymbol getIdSymbol() {
        return idSymbol;
    }

    public void setIdSymbol(IdSymbol idSymbol) {
        this.idSymbol = idSymbol;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
