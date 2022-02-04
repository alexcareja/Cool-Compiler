package cool.ast;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public class Stringg extends Expression {
    Token s;

    Stringg(ParserRuleContext ctx, Token s, Token start) {
        super(ctx, start);
        this.s = s;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
