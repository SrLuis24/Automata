import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Intermedio {

    private SymbolTable tablaSimbolos;
    private ArrayList<TreeNode> listaIntermedio = SintacticoSemantico.getListaIntermedio();
    private static int contVarTemp = 0;
    private static int contLabelTemp = 0;
    private static Stack<String> labels = new Stack<>();

    public static int getContVarTemp() {
        return ++contVarTemp;
    }

    public static int getActualVarTemp() {
        return contVarTemp;
    }

    public static int getContLabelTemp() {
        ++contLabelTemp;
        labels.add(contLabelTemp+"");
        return contLabelTemp;
    }

    public static int getActualLabelTemp() {
        return contLabelTemp;
    }

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

            if (nodo.value.equalsIgnoreCase("else")) {
                Cuadruplo goTo = new Cuadruplo("GOTO", " ", " ", "L"+getContLabelTemp());
                System.out.println(goTo);
                System.out.println("\tL" + (getActualLabelTemp()-1) + ":");
                continue;
            }

            if (nodo.value.equalsIgnoreCase("else end")) {
                System.out.println("\tL" + getActualLabelTemp() + ":");
                continue;
            }
            if (nodo.value.equalsIgnoreCase("end if")) {
                System.out.println("\tL" + labels.pop() + ":");
                continue;
            }

            InfijoPosfijo expresion = new InfijoPosfijo(nodo);

            String tipo = SintacticoSemantico.symbolTable.getEntry(nodo.lexema).type;

            String posfijo = expresion.getExpresionPosfija().toString();

            List<Cuadruplo> cuadruplos = PosfijoACuadruplos.crearCuadruplos(posfijo, tipo);
            cuadruplos.add(new Cuadruplo("Assig", "t" + getActualVarTemp(), " ", nodo.value));

            boolean pr = true;
            for (Cuadruplo c : cuadruplos) {
                if (pr) {
                    System.out.println("Expresión: " + posfijo);
                    //System.out.println("Cuadruplo: ");
                    pr = false;
                }
                System.out.println(c);
            }

        }



    }

    private void imprimirDeclaracion(TreeNode declaracion) {
            for (TreeNode b : declaracion.children) {
                //System.out.println(b.lexema + " " + tablaSimbolos.getEntry(b.lexema).type);

                Cuadruplo c = new Cuadruplo(tablaSimbolos.getEntry(b.lexema).type, b.lexema, "", null);
                System.out.println(c);
            }
    }

    private void imprimirCondicion(TreeNode condicion) {

            String op1 = condicion.children.get(0).lexema;
            String operador = condicion.children.get(1).lexema;
            String op2 = condicion.children.get(2).lexema;

            String varTemp = "t" + getContVarTemp();
            Cuadruplo c = new Cuadruplo(operador, op1, op2, varTemp);
            Cuadruplo d = new Cuadruplo("IF_FALSE", varTemp, " ", "L" + getContLabelTemp());
            System.out.println(c);
            System.out.println(d);

    }


}
