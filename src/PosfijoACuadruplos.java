import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class PosfijoACuadruplos {

    public static List<Cuadruplo> crearCuadruplos(String posfijo) {
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
                cuadruplos.add(new Cuadruplo(token, operando1, operando2, resultado));
                pila.push(resultado);
            }
        }

        return cuadruplos;
    }

}
