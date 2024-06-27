class SymbolTableEntry {
    String name;
    String type;
    int scope;
    int renglon;
    String valor;

    public SymbolTableEntry(String name, String type, int scope, int renglon) {
        this.name = name;
        this.type = type;
        this.scope = scope;
        this.renglon = renglon;
    }

    public SymbolTableEntry(String name, String type, int scope, int renglon, String valor) {
        this.name = name;
        this.type = type;
        this.scope = scope;
        this.renglon = renglon;
        this.valor = valor;
    }

    @Override
    public String toString() {
        return "Nombre: " + name + ", Tipo: " + type + ", Alcance: " + scope + ", Renglón: " + renglon;
    }
}
