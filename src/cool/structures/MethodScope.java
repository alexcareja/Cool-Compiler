package cool.structures;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class MethodScope extends Symbol implements Scope{
    // Fiecare identificator posedă un tip.
    protected TypeSymbol returnType;
    protected TypeSymbol parent;
    protected Map<String, IdSymbol> formals;
    private int offset;
    MethodScope oldMethodScope;

    public MethodScope(String name) {
        super(name);
        formals = new LinkedHashMap<>();
    }

    public ArrayList<IdSymbol> getFormals() {
        return new ArrayList<>(formals.values());
    }

    public TypeSymbol getReturnType() {
        return returnType;
    }

    public TypeSymbol getParent() {
        return parent;
    }

    public int getOffset() {
        return offset;
    }

    public MethodScope getOldMethodScope() {
        return oldMethodScope;
    }

    public void setParent(TypeSymbol parentType) {
        this.parent = parentType;
    }

    public void setReturnType(TypeSymbol returnType) {
        this.returnType = returnType;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setOldMethodScope(MethodScope oldMethodScope) {
        this.oldMethodScope = oldMethodScope;
    }

    @Override
    public boolean add(Symbol sym) {
        if (formals.get(sym.getName()) != null) {
            return false;
        }

        formals.put(sym.getName(), (IdSymbol) sym);

        return true;
    }

    @Override
    public Symbol lookup(String str) {
        IdSymbol sym = formals.get(str);

        if (sym != null) {
            return sym;
        }

        return parent.lookup(str);
    }
}
