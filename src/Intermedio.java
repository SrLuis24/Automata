import java.util.ArrayList;
import java.util.List;

public class Intermedio {

    private SymbolTable tablaSimbolos;
    private ArrayList<TreeNode> listaIntermedio = SintacticoSemantico.getListaIntermedio();

    public Intermedio(SymbolTable tablaSimbolos) {
        this.tablaSimbolos = tablaSimbolos;
        //System.out.println(listaIntermedio + " <--");

        for (TreeNode nodo : listaIntermedio) {

            //System.out.println(nodo.value + " <-----");
            if (nodo.value.equalsIgnoreCase("declaraciones")) {
                imprimirDeclaracion(nodo);
                continue;
            }

            if (nodo.value.equalsIgnoreCase("condiciones")) {
                imprimirCondicion(nodo);
                continue;
            }

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

    private void imprimirDeclaracion(TreeNode declaracion) {
            for (TreeNode b : declaracion.children) {
                //System.out.println(b.lexema + " " + tablaSimbolos.getEntry(b.lexema).type);

                Cuadruplo c = new Cuadruplo(tablaSimbolos.getEntry(b.lexema).type, b.lexema, "", null);
                System.out.println("Cuadruplo");
                System.out.println(c);
                System.out.println("");
            }
    }

    private void imprimirCondicion(TreeNode condicion) {

            String op1 = condicion.children.get(0).lexema;
            String operador = condicion.children.get(1).lexema;
            String op2 = condicion.children.get(2).lexema;

            Cuadruplo c = new Cuadruplo(operador, op1, op2, "");
            System.out.println("Cuadruplo");
            System.out.println(c);
            System.out.println("");

    }


}
