package cool.structures;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

public class TypeSymbol extends Symbol implements Scope {
    protected Map<String, IdSymbol> attributes = new LinkedHashMap<>();
    protected Map<String, MethodScope> methods = new LinkedHashMap<>();
    protected ArrayList<TypeSymbol> children = new ArrayList<>();
    String parentName;
    TypeSymbol parent;
    public static int currentTag = 0;
    private boolean set = false;
    private int tag;
    private int maxTag;

    public TypeSymbol(String name) {
        super(name);

        setTypeParents();
        initSelf();
    }

    public TypeSymbol(String name, String parentName) {
        super(name);
        this.parentName = parentName;

        setTypeParents();
        initSelf();
    }

    public String getName() {
        return this.name;
    }

    @Override
    public Scope getParent() {
        return parent;
    }

    public int getTag() {
        return tag;
    }

    public int getMaxTag() {
        return maxTag;
    }

    public String getParentName() {
        return parentName;
    }

    public int getAttributesSize() {
        int size = 4 * attributes.size()  - 4;

        if (parent != null) {
            size += parent.getAttributesSize();
        }

        return size;
    }

    public int countParentMethods() {
        TypeSymbol parent = this.parent;
        HashSet<String> methodNamesSet = new HashSet<>();

        while (parent != null) {
            methodNamesSet.addAll(parent.methods.keySet());
            parent = (TypeSymbol) parent.getParent();
        }

        return methodNamesSet.size();
    }

    public void setParent(TypeSymbol parentType) {
        this.parent = parentType;

        if (this != Primitive.SELF_TYPE) {
            parent.children.add(this);
        }
    }

    private void initSelf() {
        IdSymbol self = new IdSymbol("self", null);
        self.setType(Primitive.SELF_TYPE);
        attributes.put("self", self);
    }

    private void setTypeParents() {
        if (!set) {
            set = true;
        }
    }

    public int setTag() {
        tag = currentTag;
        maxTag = currentTag++;

        for (TypeSymbol child : children) {
            if (child != Primitive.SELF_TYPE) {
                int childTag = child.setTag();
                if (maxTag < childTag) {
                    maxTag = childTag;
                }
            }
        }

        return maxTag;
    }

    @Override
    public boolean add(Symbol sym) {
        if (attributes.get(sym.getName()) != null) {
            return false;
        }

        attributes.put(sym.getName(), (IdSymbol) sym);

        return true;
    }

    @Override
    public Symbol lookup(String name) {
        IdSymbol sym = attributes.get(name);

        if (sym != null) {
            return sym;
        }

        if (parent != null) {
            return parent.lookup(name);
        }

        return null;
    }

    public MethodScope lookupMethod(String name) {
        MethodScope sym = methods.get(name);

        if (sym != null) {
            return sym;
        }

        if (parent != null) {
            return parent.lookupMethod(name);
        }

        return null;
    }

    public boolean addMethod(MethodScope methodScope) {
        if (methods.get(methodScope.name) != null) {
            return false;
        }
        methods.put(methodScope.name, methodScope);

        return true;
    }

    public boolean doesNotRelate(TypeSymbol sym) {
        if (parent == sym) {
            return false;
        }

        TypeSymbol s = parent;
        if (s == null) {
            return true;
        }

        do {
            s = (TypeSymbol) s.getParent();
            if (s == sym) {
                return false;
            }
        } while (s != null);

        return true;
    }

    public TypeSymbol leastCommonAncestor(TypeSymbol sym) {
        ArrayList<TypeSymbol> parents = new ArrayList<>();
        TypeSymbol s = this;

        if (sym == null) {
            return this;
        }

        while (s != null) {
            parents.add(s);
            s = (TypeSymbol) s.getParent();
        }

        while (sym != null) {
            if (parents.contains(sym)) {
                return sym;
            }
            sym = (TypeSymbol) sym.getParent();
        }

        return Primitive.OBJECT;
    }

    public void clearChildren() {
        children.clear();
    }

    public ST constructor(STGroupFile templates) {
        return templates.getInstanceOf("constructor")
                .add("class", name)
                .add("parent", getParentName());
    }

    private ArrayList<String> getAllMethodNames() {
        ArrayList<String> methodNames = new ArrayList<>();
        var currentClassMethods = this.methods.keySet();

        if (parent != null) {
            for (String s : parent.getAllMethodNames()) {
                String methodName = s.substring(s.indexOf(".") + 1);
                if (!this.methods.containsKey(methodName)) {
                    methodNames.add(s);
                } else {
                    methodNames.add(this.name + "." + methodName);
                    currentClassMethods.remove(methodName);
                }
            }
        }

        for (String s : currentClassMethods) {
            methodNames.add(this.name + "." + s);
        }

        return methodNames;
    }

    public ST dispatchTable(STGroupFile templates) {
        ST st = templates.getInstanceOf("sequence");

        for (String method : getAllMethodNames()) {
            st.add("e", templates.getInstanceOf("dispTabField")
                    .add("method", method));
        }

        return templates.getInstanceOf("dispTab")
                .add("class", name)
                .add("methods", st);
    }

    private String getAttributesMIPS() {
        if (this == Primitive.STRING) {
            return "\t.word int_const_0\n\t.asciiz \"\"\n\t.align 2";
        }
        if (this == Primitive.INT || this == Primitive.BOOL) {
            return "\t.word 0";
        }

        StringBuilder attributes = new StringBuilder();

        for (IdSymbol attribute : this.attributes.values()) {
            if (!attribute.getName().equals("self")) {
                TypeSymbol type = attribute.getType();

                if (type == Primitive.STRING) {
                    attributes.append("\t.word str_const_0");
                } else if (type == Primitive.INT) {
                    attributes.append("\t.word int_const_0");
                } else if (type == Primitive.BOOL) {
                    attributes.append("\t.word bool_const_0");
                } else {
                    attributes.append("\t.word 0");
                }
                attributes.append("\n");
            }
        }

        if (parent != null) {
            return parent.getAttributesMIPS() + attributes;
        }

        return attributes.toString();
    }

    public ST protObj(STGroupFile templates) {
        int words = 3;
        TypeSymbol type = this;

        while (type != null) {
            words += type.attributes.size() - 1;
            type = (TypeSymbol) type.getParent();
        }

        return templates.getInstanceOf("protObj")
                .add("class", this.name)
                .add("tag", this.tag)
                .add("words", words)
                .add("attributes", getAttributesMIPS());
    }

}
