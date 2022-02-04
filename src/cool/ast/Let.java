package cool.ast;

import cool.structures.LetScope;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import java.util.ArrayList;

public class Let extends Expression {
    ArrayList<VarDef> vars;
    Expression body;
    LetScope scope;

    Let(ParserRuleContext ctx, ArrayList<VarDef> vars, Expression body, Token start) {
        super(ctx, start);
        this.vars = new ArrayList<>(vars);
        this.body = body;
    }

    public ArrayList<VarDef> getVars() {
        return vars;
    }

    public Expression getBody() {
        return body;
    }

    public LetScope getScope() {
        return scope;
    }

    public void setScope(LetScope scope) {
        this.scope = scope;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
