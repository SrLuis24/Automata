import java.util.ArrayList;
import java.util.Stack;

public class InfijoPosfijo {

    private ArrayList<String> expresiones = new ArrayList<>();
    private Stack<String> pila = new Stack<>();
    private StringBuilder exPosfija = new StringBuilder();

    public InfijoPosfijo(TreeNode expresion) {

        expresiones.add(expresion.value);

        for (TreeNode nodo : expresion.children) {
            expresiones.add(nodo.value);
        }

        expresiones.remove(0);
        expresiones.remove(0);
        System.out.println(expresiones);

        convertirPosfijo();

    }

    private void convertirPosfijo() {

        for (String expresion : expresiones) {
            expresion = expresion.replace(" ", "");
            if (isOperador(expresion)) {

                if (pila.isEmpty()) {
                    pila.push(expresion);
                    continue;
                }

                int pE = prioridadEnExpresion(expresion);
                System.out.println("pE " + pE);
                int pP = prioridadEnExpresion(pila.lastElement());
                System.out.println("pP " + pP);

                if (pE == 0) {
                    pila.push(expresion);
                } else if (pE == 4) {

                    for (int i=0; i <= pila.size(); i++) {
                        String op = pila.pop();
                        if (op.equals("(")) {
                            break;
                        }
                        exPosfija.append(op);
                    }
                } else if (pE == pP) {
                    String opEnPila = pila.pop();
                    pila.push(expresion);

                    exPosfija.append(opEnPila);
                } else if (pE > pP) {
                    pila.push(expresion);
                } else if (pE < pP) {
                    for (int i = 0; i < pila.size(); i++) {
                        String opEnPila = pila.pop();
                        System.out.println(opEnPila + " <-");
                        if (opEnPila.equals("(") || opEnPila.equals(")")) {
                            break;
                        }
                        exPosfija.append(opEnPila);
                    }
                }


            } else {
                exPosfija.append(expresion);
            }

        }

        if (!pila.isEmpty()) {
            for (int i = 0; i <= pila.size(); i++) {
                exPosfija.append(pila.pop());
            }
        }

        System.out.println(exPosfija + "\n");



    }

    private boolean isOperador(String letra) {
        if (letra.equals("^") || letra.equals("*") || letra.equals("/") || letra.equals("+")|| letra.equals("-") || letra.equals("(") || letra.equals(")")) {
            return true;
        } else {
            return false;
        }
    }

    private int prioridadEnExpresion(String letra) {
        if (letra.equals("(")) {
            return 0;
        } else if (letra.equals(")")) {
            return 4;
        } else if (letra.equals("^")) {
            return 3;
        } else if (letra.equals("*") || letra.equals("/")) {
            return 2;
        } else if (letra.equals("+") || letra.equals("-")) {
            return 1;
        } else {
            System.out.println(letra);
            return 0;
        }
    }

}
