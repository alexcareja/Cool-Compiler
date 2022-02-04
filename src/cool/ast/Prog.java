package cool.ast;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import java.util.ArrayList;

public class Prog extends ASTNode {
    ArrayList<ClassDef> classes;
    Token token;
    ParserRuleContext ctx;

    Prog(ParserRuleContext ctx, ArrayList<ClassDef> classes, Token start) {
        this.ctx = ctx;
        this.token = start;
        this.classes = new ArrayList<>(classes);
    }

    public ArrayList<ClassDef> getClasses() {
        return classes;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
