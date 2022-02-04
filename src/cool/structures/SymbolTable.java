package cool.structures;

import java.io.File;

import cool.parser.CoolParser;
import org.antlr.v4.runtime.*;

import cool.compiler.Compiler;

public class SymbolTable {
    public static Scope globalScope;
    
    private static boolean semanticErrors;
    
    public static void defineBasicClasses() {
        globalScope = new DefaultScope(null);
        semanticErrors = false;

        Primitive.OBJECT = new TypeSymbol("Object");
        Primitive.INT   = new TypeSymbol("Int");
        Primitive.STRING = new TypeSymbol("String");
        Primitive.BOOL  = new TypeSymbol("Bool");
        Primitive.IO = new TypeSymbol("IO");
        Primitive.SELF_TYPE = new TypeSymbol("SELF_TYPE");

        Primitive.OBJECT.clearChildren();

        Primitive.INT.setParent(Primitive.OBJECT);
        Primitive.STRING.setParent(Primitive.OBJECT);
        Primitive.BOOL.setParent(Primitive.OBJECT);
        Primitive.IO.setParent(Primitive.OBJECT);
        Primitive.SELF_TYPE.setParent(Primitive.OBJECT);
        
        // TODO Populate global scope.
        globalScope.add(Primitive.OBJECT);
        globalScope.add(Primitive.INT);
        globalScope.add(Primitive.STRING);
        globalScope.add(Primitive.BOOL);
        globalScope.add(Primitive.IO);
        globalScope.add(Primitive.SELF_TYPE);

        initObject();
        initString();
        initIO();
    }

    private static void initObject() {
        MethodScope m = new MethodScope("abort");
        m.setParent(Primitive.OBJECT);
        m.setReturnType(Primitive.OBJECT);
        Primitive.OBJECT.addMethod(m);
        m.setOffset(0);

        m = new MethodScope("copy");
        m.setParent(Primitive.OBJECT);
        m.setReturnType(Primitive.SELF_TYPE);
        Primitive.OBJECT.addMethod(m);
        m.setOffset(4);

        m = new MethodScope("type_name");
        m.setParent(Primitive.OBJECT);
        m.setReturnType(Primitive.STRING);
        Primitive.OBJECT.addMethod(m);
        m.setOffset(8);
    }

    private static void initString() {
        MethodScope m = new MethodScope("concat");
        m.setParent(Primitive.STRING);
        m.setReturnType(Primitive.STRING);
        IdSymbol symbol = new IdSymbol("x", null);
        symbol.setType(Primitive.STRING);
        m.add(symbol);
        Primitive.STRING.addMethod(m);
        m.setOffset(12);

        m = new MethodScope("length");
        m.setParent(Primitive.STRING);
        m.setReturnType(Primitive.INT);
        Primitive.STRING.addMethod(m);
        m.setOffset(16);

        m = new MethodScope("substr");
        m.setParent(Primitive.STRING);
        m.setReturnType(Primitive.STRING);
        symbol = new IdSymbol("i", null);
        symbol.setType(Primitive.INT);
        m.add(symbol);
        symbol = new IdSymbol("l", null);
        symbol.setType(Primitive.INT);
        m.add(symbol);
        Primitive.STRING.addMethod(m);
        m.setOffset(20);
}

    private static void initIO() {
        MethodScope m= new MethodScope("out_int");
        m.setParent(Primitive.IO);
        m.setReturnType(Primitive.SELF_TYPE);
        IdSymbol symbol = new IdSymbol("x", null);
        symbol.setType(Primitive.INT);
        m.add(symbol);
        Primitive.IO.addMethod(m);
        m.setOffset(12);

        m = new MethodScope("in_int");
        m.setParent(Primitive.IO);
        m.setReturnType(Primitive.INT);
        Primitive.IO.addMethod(m);
        m.setOffset(16);

        m = new MethodScope("out_string");
        m.setParent(Primitive.IO);
        m.setReturnType(Primitive.SELF_TYPE);
        symbol = new IdSymbol("x", null);
        symbol.setType(Primitive.STRING);
        m.add(symbol);
        Primitive.IO.addMethod(m);
        m.setOffset(20);

        m = new MethodScope("in_string");
        m.setParent(Primitive.IO);
        m.setReturnType(Primitive.STRING);
        Primitive.IO.addMethod(m);
        m.setOffset(24);
    }

    /**
     * Displays a semantic error message.
     * 
     * @param ctx Used to determine the enclosing class context of this error,
     *            which knows the file name in which the class was defined.
     * @param info Used for line and column information.
     * @param str The error message.
     */
    public static void error(ParserRuleContext ctx, Token info, String str) {
        while (! (ctx.getParent() instanceof CoolParser.ProgramContext))
            ctx = ctx.getParent();
        
        String message = "\"" + new File(Compiler.fileNames.get(ctx)).getName()
                + "\", line " + info.getLine()
                + ":" + (info.getCharPositionInLine() + 1)
                + ", Semantic error: " + str;
        
        System.err.println(message);
        
        semanticErrors = true;
    }
    
    public static void error(String str) {
        String message = "Semantic error: " + str;
        
        System.err.println(message);
        
        semanticErrors = true;
    }
    
    public static boolean hasSemanticErrors() {
        return semanticErrors;
    }
}
