package cool.ast;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import java.util.ArrayList;

public class Case extends Expression {
    Expression value;
    ArrayList<CaseDef> cases;

    Case(ParserRuleContext ctx, Expression value, ArrayList<CaseDef> cases, Token start) {
        super(ctx, start);
        this.value = value;
        this.cases = new ArrayList<>(cases);
    }

    public Expression getValue() {
        return value;
    }

    public ArrayList<CaseDef> getCases() {
        return cases;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
