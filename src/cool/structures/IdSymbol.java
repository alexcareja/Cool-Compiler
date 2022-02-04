package cool.structures;

import org.antlr.v4.runtime.Token;

public class IdSymbol extends Symbol {
    // Fiecare identificator posedă un tip.
    protected TypeSymbol type;
    protected Token typeToken;
    protected int offset;
    protected boolean isAttribute;
    
    public IdSymbol(String name, Token typeToken) {
        super(name);
        this.typeToken = typeToken;
    }

    public TypeSymbol getType() {
        return type;
    }

    public Token getTypeToken() {
        return typeToken;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setAsAttribute(boolean attribute) {
        this.isAttribute = attribute;
    }

    public void setType(TypeSymbol type) {
        this.type = type;
    }

    public boolean isAttribute() {
        return isAttribute;
    }
}