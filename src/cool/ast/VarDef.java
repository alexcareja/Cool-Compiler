package cool.ast;

import cool.structures.IdSymbol;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

// Definitii de variabile.
public class VarDef extends Definition {
    Token id;
    Token type;
    Expression value = null;
    IdSymbol idSymbol;

    VarDef(ParserRuleContext ctx, Token id, Token type, Token start) {
        super(ctx, start);
        this.id = id;
        this.type = type;
    }

    VarDef(ParserRuleContext ctx, Token id, Token type, Expression value, Token start) {
        super(ctx, start);
        this.id = id;
        this.type = type;
        this.value = value;
    }

    public String getId() {
        return this.id.getText();
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

    public Expression getValue() {
        return value;
    }

    public Token getValueToken() {
        return value.token;
    }

    public IdSymbol getIdSymbol() {
        return idSymbol;
    }

    public void setIdSymbol(IdSymbol idSymbol) {
        this.idSymbol = idSymbol;
    }

    public boolean hasInitValue() {
        return value != null;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
