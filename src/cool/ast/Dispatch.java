package cool.ast;

import cool.structures.TypeSymbol;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import java.util.ArrayList;

public class Dispatch extends Expression {
    Token id;
    ArrayList<Expression> args;
    TypeSymbol callerType;

    Dispatch(ParserRuleContext ctx, Token id, ArrayList<Expression> args, Token start) {
        super(ctx, start);
        this.id = id;
        this.args = new ArrayList<>(args);
    }

    public String getId() {
        return id.getText();
    }

    public Token getIdToken() {
        return id;
    }

    public ArrayList<Expression> getArgs() {
        return args;
    }

    public TypeSymbol getCallerType() {
        return callerType;
    }

    public void setCallerType(TypeSymbol callerType) {
        this.callerType = callerType;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
