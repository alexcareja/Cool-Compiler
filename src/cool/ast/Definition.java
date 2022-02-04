package cool.ast;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public abstract class Definition extends ASTNode {
    Token token;
    ParserRuleContext ctx;

    Definition(ParserRuleContext ctx, Token token) {
        this.token = token;
        this.ctx = ctx;
    }

    public ParserRuleContext getContext() {
        return ctx;
    }

    public Token getToken() {
        return token;
    }
}
