package cool.ast;

import cool.structures.IdSymbol;
import cool.structures.MethodScope;
import cool.structures.TypeSymbol;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import java.util.ArrayList;

// Definitii de atribute si metode
public class ClassElement extends Definition {
    Token id;
    Token type;
    Expression value = null;
    ArrayList<Formal> params = null;
    ArrayList<Expression> body = null;
    IdSymbol idSymbol;
    MethodScope methodScope;

    ClassElement(ParserRuleContext ctx, Token id, Token type, Token start) {
        super(ctx, start);
        this.id = id;
        this.type = type;
    }

    ClassElement(ParserRuleContext ctx, Token id, Token type, Expression value, Token start) {
        super(ctx, start);
        this.id = id;
        this.type = type;
        this.value = value;
    }

    ClassElement(ParserRuleContext ctx, Token id, ArrayList<Formal> params, Token type, ArrayList<Expression> body, Token start) {
        super(ctx, start);
        this.id = id;
        this.params = new ArrayList<>(params);
        this.type = type;
        this.body = new ArrayList<>(body);
    }

    public String getName() {
        return id.getText();
    }

    public String getType() {
        return type.getText();
    }

    public ArrayList<Formal> getParams() {
        return params;
    }

    public ArrayList<Expression> getBody() {
        return body;
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

    public MethodScope getMethodScope() {
        return methodScope;
    }

    public Token getIdToken() {
        return id;
    }

    public Token getTypeToken() {
        return type;
    }

    public Token getBodyTypeToken() {
        return body.get(body.size() - 1).token;
    }

    public void setIdSymbol(IdSymbol idSymbol) {
        this.idSymbol = idSymbol;
    }

    public void setMethodSymbol(MethodScope methodSymbol) {
        this.methodScope = methodSymbol;
    }

    public boolean isMethod() {
        return this.params != null;
    }

    public boolean hasInitValue() {
        return this.value != null;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
