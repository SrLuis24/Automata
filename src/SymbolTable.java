import java.util.ArrayList;

class SymbolTable {
    private ArrayList<SymbolTableEntry> tabla;

    public SymbolTable() {
        tabla = new ArrayList<>();
    }

    public void addEntry(String name, String type, int token, int renglon) {
        SymbolTableEntry entrada = new SymbolTableEntry(name, type, token, renglon);
        tabla.add(entrada);
    }

    public void addEntry(String name, String type, int token, int renglon, String valor) {
        SymbolTableEntry entrada = new SymbolTableEntry(name, type, token, renglon, valor);
        tabla.add(entrada);
    }

    public SymbolTableEntry getEntry(String name) {

        String entrada;
        for (int i = 0; i < tabla.size(); i++) {
            entrada = tabla.get(i).name;

            if (entrada.equals(name)){
                return tabla.get(i);
            }
        }
        return null;
    }

    public boolean contains(String name) {
        String entrada;
        for (int i = 0; i < tabla.size(); i++) {
            entrada = tabla.get(i).name;

            if (entrada.equals(name)){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        tabla.forEach(entry -> {
            sb.append(entry.toString()).append("\n");
        });

        return sb.toString();
    }
}
