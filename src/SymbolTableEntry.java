class SymbolTableEntry {
    String name;
    String type;
    int token;
    int renglon;
    String valor;

    public SymbolTableEntry(String name, String type, int token, int renglon) {
        this.name = name;
        this.type = type;
        this.token = token;
        this.renglon = renglon;
    }

    public SymbolTableEntry(String name, String type, int token, int renglon, String valor) {
        this.name = name;
        this.type = type;
        this.token = token;
        this.renglon = renglon;
        this.valor = valor;
    }

    @Override
    public String toString() {
        //if (valor == null) {
            return "Nombre: " + name + ", Tipo: " + type + ", Token: " + token + ", Renglón: " + renglon;
        //}
        //return "Nombre: " + name + ", Tipo: " + type + ", Token: " + token + ", Renglón: " + renglon + ", Valor: " + valor;
    }
}
