package cool.ast;

import cool.structures.TypeSymbol;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import java.util.ArrayList;

public class StaticDispatch extends Expression {
    Expression invoker;
    Token type = null;
    Token id;
    ArrayList<Expression> args;
    TypeSymbol callerType;

    StaticDispatch(ParserRuleContext ctx, Expression invoker, Token id, ArrayList<Expression> args, Token start) {
        super(ctx, start);
        this.invoker = invoker;
        this.id = id;
        this.args = new ArrayList<>(args);
    }

    StaticDispatch(ParserRuleContext ctx, Expression invoker, Token type, Token id, ArrayList<Expression> args, Token start) {
        super(ctx, start);
        this.invoker = invoker;
        this.type = type;
        this.id = id;
        this.args = new ArrayList<>(args);
    }

    public Expression getInvoker() {
        return invoker;
    }

    public String getId() {
        return id.getText();
    }

    public Token getIdToken() {
        return id;
    }

    public String getType() {
        return type.getText();
    }

    public Token getTypeToken() {
        return type;
    }

    public TypeSymbol getCallerType() {
        return callerType;
    }

    public ArrayList<Expression> getArgs() {
        return args;
    }

    public void setCallerType(TypeSymbol callerType) {
        this.callerType = callerType;
    }

    public boolean hasType() {
        return type != null;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
