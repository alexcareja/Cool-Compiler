package cool.structures;

import java.util.LinkedHashMap;
import java.util.Map;

public class LetScope implements Scope{
    protected Scope parent;
    protected Map<String, IdSymbol> vars;

    public LetScope(Scope parent) {
        this.parent = parent;
        this.vars = new LinkedHashMap<>();
    }

    @Override
    public Scope getParent() {
        return parent;
    }

    @Override
    public boolean add(Symbol sym) {
        if (vars.get(sym.getName()) != null) {
            return false;
        }

        vars.put(sym.getName(), (IdSymbol) sym);

        return true;
    }

    @Override
    public Symbol lookup(String str) {
        Symbol sym = vars.get(str);

        if (sym == null) {
            return parent.lookup(str);
        }

        return sym;
    }
}
