package cool.ast;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public abstract class Expression extends ASTNode {
    // Reținem un token descriptiv al expresiei, pentru a putea afișa ulterior
    // informații legate de linia și coloana eventualelor erori semantice.
    Token token;
    ParserRuleContext ctx;

    Expression(ParserRuleContext ctx, Token token) {
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
