package cool.ast;

import cool.structures.SymbolTable;
import cool.structures.TypeSymbol;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import java.util.ArrayList;

// Definitii de clase.
public class ClassDef extends Definition {
    Token type;
    Token parent = null;
    ArrayList<ClassElement> body;
    TypeSymbol scope;

    ClassDef(ParserRuleContext ctx, Token type, ArrayList<ClassElement> body, Token start) {
        super(ctx, start);
        this.type = type;
        this.body = new ArrayList<>(body);
    }

    ClassDef(ParserRuleContext ctx, Token type, Token parent, ArrayList<ClassElement> body, Token start) {
        super(ctx, start);
        this.type = type;
        this.parent = parent;
        this.body = new ArrayList<>(body);
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public Token getParent() {
        return this.parent;
    }

    public String getType() {
        return this.type.getText();
    }

    public Token getTypeToken() {
        return this.type;
    }

    public Token getParentToken() {
        return this.parent;
    }

    public ArrayList<ClassElement> getBody() {
        return body;
    }

    public TypeSymbol getScope() {
        return scope;
    }

    public void setScope(TypeSymbol classScope) {
        this.scope = classScope;
    }
}
