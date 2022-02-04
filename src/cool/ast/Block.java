package cool.ast;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import java.util.ArrayList;

public class Block extends Expression {
    ArrayList<Expression> body;

    Block(ParserRuleContext ctx, ArrayList<Expression> body, Token start) {
        super(ctx, start);
        this.body = new ArrayList<>(body);
    }

    public ArrayList<Expression> getBody() {
        return body;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
