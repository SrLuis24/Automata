import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class PosfijoACuadruplos {

    public static List<Cuadruplo> crearCuadruplos(String posfijo, String tipo) {
        Stack<String> pila = new Stack<>();
        List<Cuadruplo> cuadruplos = new ArrayList<>();
        //String[] tokens = posfijo.split("(?<=[-+*/])|(?=[-+*/])");
        String[] tokens = posfijo.split(",");
        int contVarTemp = 1;
        //System.out.println(Arrays.toString(tokens) + " <-");

        for (String token : tokens) {
            if (token.matches("\\d+(\\.\\d+)?")) {
                pila.push(token);
            } else {
                //System.out.println(pila);
                String operando2 = pila.pop();
                String operando1 = pila.pop();
                String resultado = "t" + contVarTemp++;

                //String valor = realizarOperacion(tipo, token, operando1, operando2);

                cuadruplos.add(new Cuadruplo(token, operando1, operando2, resultado));
                pila.push(resultado);
            }
        }

        return cuadruplos;
    }

    private static String realizarOperacion(String tipo, String operador, String operando1, String operando2) {

        String valor = "";

        if (tipo.equals("Int")) {
            int r = 0;
            int op1 = Integer.parseInt(operando1);
            int op2 = Integer.parseInt(operando2);
            if (operador.equals("+")) {
                r = op1 + op2;
            } else if (operador.equals("-")) {
                r = op1 - op2;
            } else if (operador.equals("*")) {
                r = op1 * op2;
            } else if (operador.equals("/")) {
                r = op1 / op2;
            }

            valor = String.valueOf(r);
        } else if (tipo.equals("Real")) {
            float r = 0;
            float op1 = Float.parseFloat(operando1);
            float op2 = Float.parseFloat(operando2);
            if (operador.equals("+")) {
                r = op1 + op2;
            } else if (operador.equals("-")) {
                r = op1 - op2;
            } else if (operador.equals("*")) {
                r = op1 * op2;
            } else if (operador.equals("/")) {
                r = op1 / op2;
            }

            valor = String.valueOf(r);
        }

        return valor;
    }

}
