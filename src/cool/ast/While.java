package cool.ast;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import java.util.ArrayList;

public class While extends Expression {
    Expression cond;
    ArrayList<Expression> body;

    While(ParserRuleContext ctx, Expression cond, ArrayList<Expression> body, Token start) {
        super(ctx, start);
        this.cond = cond;
        this.body = new ArrayList<>(body);
    }

    public Expression getCond() {
        return cond;
    }

    public Token getCondToken() {
        return cond.token;
    }

    public ArrayList<Expression> getBody() {
        return body;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
