class SymbolTableEntry {
    String name;
    String type;
    int scope;

    public SymbolTableEntry(String name, String type, int scope) {
        this.name = name;
        this.type = type;
        this.scope = scope;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Type: " + type + ", Scope: " + scope;
    }
}
