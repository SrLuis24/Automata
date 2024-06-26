import java.util.HashMap;
import java.util.Map;

class SymbolTable {
    private Map<String, SymbolTableEntry> table;

    public SymbolTable() {
        table = new HashMap<>();
    }

    public void addEntry(String name, String type, int scope) {
        SymbolTableEntry entry = new SymbolTableEntry(name, type, scope);
        table.put(name, entry);
    }

    public SymbolTableEntry getEntry(String name) {
        return table.get(name);
    }

    public boolean contains(String name) {
        return table.containsKey(name);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (SymbolTableEntry entry : table.values()) {
            sb.append(entry.toString()).append("\n");
        }
        return sb.toString();
    }
}
