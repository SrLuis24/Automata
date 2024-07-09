import java.util.ArrayList;
import java.util.List;

public class Intermedio {

    private ArrayList<TreeNode> asignaciones = SintacticoSemantico.getAsignaciones();

    public Intermedio() {

        for (TreeNode nodo : asignaciones) {
            InfijoPosfijo expresion = new InfijoPosfijo(nodo);

            System.out.println(SintacticoSemantico.symbolTable.getEntry(nodo.lexema).type);

            String posfijo = expresion.getExpresionPosfija().toString();

            List<Cuadruplo> cuadruplos = PosfijoACuadruplos.crearCuadruplos(posfijo);

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


}
