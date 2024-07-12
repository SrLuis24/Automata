import java.util.ArrayList;
import java.util.List;

public class Intermedio {

    private SymbolTable tablaSimbolos;
    private ArrayList<TreeNode> asignaciones = SintacticoSemantico.getAsignaciones();
    private ArrayList<TreeNode> declaraciones = SintacticoSemantico.getDeclaraciones();

    public Intermedio(SymbolTable tablaSimbolos) {
        this.tablaSimbolos = tablaSimbolos;

        imprimirDeclaraciones();

        for (TreeNode nodo : asignaciones) {
            InfijoPosfijo expresion = new InfijoPosfijo(nodo);

            String tipo = SintacticoSemantico.symbolTable.getEntry(nodo.lexema).type;

            String posfijo = expresion.getExpresionPosfija().toString();

            List<Cuadruplo> cuadruplos = PosfijoACuadruplos.crearCuadruplos(posfijo, tipo);

            boolean pr = true;
            for (Cuadruplo c : cuadruplos) {
                if (pr) {
                    System.out.println("Expresión: " + posfijo);
                    System.out.println("Cuadruplo: ");
                    pr = false;
                }
                System.out.println(c);
            }
            System.out.println("");

        }



    }

    private void imprimirDeclaraciones() {
        for (TreeNode a : declaraciones) {

            for (TreeNode b : a.children) {
                //System.out.println(b.lexema + " " + tablaSimbolos.getEntry(b.lexema).type);

                Cuadruplo c = new Cuadruplo(tablaSimbolos.getEntry(b.lexema).type, b.lexema, "", null);
                System.out.println("Cuadruplo");
                System.out.println(c);
                System.out.println("");
            }
        }
    }


}
