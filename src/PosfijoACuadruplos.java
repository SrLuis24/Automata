import java.util.*;

public class PosfijoACuadruplos {

    private static Map<String, String> valoresT = new HashMap<>();

    public static List<Cuadruplo> crearCuadruplos(String posfijo, String tipo) {
        Stack<String> pila = new Stack<>();
        List<Cuadruplo> cuadruplos = new ArrayList<>();
        //String[] tokens = posfijo.split("(?<=[-+*/])|(?=[-+*/])");
        String[] tokens = posfijo.split(",");
        int contVarTemp = 1;


        for (String token : tokens) {
            if (token.matches("\\d+(\\.\\d+)?")) {
                pila.push(token);
            } else {

                String operando2 = pila.pop();
                String operando1 = pila.pop();
                String resultado = "t" + contVarTemp++;

                String valor = realizarOperacion(tipo, token, operando1, operando2);
                valoresT.put(resultado, valor);

                cuadruplos.add(new Cuadruplo(token, operando1, operando2, resultado, valor));
                pila.push(resultado);

            }
        }

        return cuadruplos;
    }

    private static String realizarOperacion(String tipo, String operador, String operando1, String operando2) {

        String valor = "";

        if (tipo.equals("Int")) {
            int r = 0;
            int op1 = 0;
            int op2 = 0;

            if (operando1.startsWith("t")) {
                op1 = Integer.parseInt(valoresT.get(operando1));
            } else {
                op1 = Integer.parseInt(operando1);
            }

            if (operando2.startsWith("t")) {
                op2 = Integer.parseInt(valoresT.get(operando2));
            } else {
                op2 = Integer.parseInt(operando2);
            }

            if (operador.equals("+")) {
                r = op1 + op2;
            } else if (operador.equals("-")) {
                r = op1 - op2;
            } else if (operador.equals("*")) {
                //System.out.println("Entre");
                r = op1 * op2;
                //System.out.println("R (*): " + r );
            } else if (operador.equals("/")) {
                r = op1 / op2;
                //System.out.println("Entre. op1:" + op1 + "  op2:" + op2);
                //System.out.println("R (/): " + r );
            }

            valor = String.valueOf(r);
        } else if (tipo.equals("Real")) {
            float r = 0;
            float op1 = 0;
            float op2 = 0;

            if (operando1.startsWith("t")) {
                op1 = Float.parseFloat(valoresT.get(operando1));
            } else {
                op1 = Float.parseFloat(operando1);
            }

            if (operando2.startsWith("t")) {
                op2 = Float.parseFloat(valoresT.get(operando2));
            } else {
                op2 = Float.parseFloat(operando2);
            }

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
