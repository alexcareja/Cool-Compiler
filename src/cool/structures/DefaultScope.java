package cool.structures;

import java.util.*;

public class DefaultScope implements Scope {

    protected Map<String, Symbol> symbols = new LinkedHashMap<>();

    protected Scope parent;
    
    public DefaultScope(Scope parent) {
        this.parent = parent;
    }

    @Override
    public Scope getParent() {
        return parent;
    }

    public Map<String, Symbol> getSymbols() {
        return symbols;
    }

    @Override
    public boolean add(Symbol sym) {
        // Reject duplicates in the same scope.
        if (symbols.containsKey(sym.getName()))
            return false;
        
        symbols.put(sym.getName(), sym);
        
        return true;
    }

    @Override
    public Symbol lookup(String name) {
        var sym = symbols.get(name);

        if (sym != null)
            return sym;

        if (parent != null)
            return parent.lookup(name);

        return null;
    }
    
    @Override
    public String toString() {
        return symbols.values().toString();
    }

}
