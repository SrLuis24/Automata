import java.util.*;

public class PosfijoACuadruplosOptimizado {

    private static Map<String, String> valoresT = new HashMap<>();

    public static List<Cuadruplo> crearCuadruplosOptimizado(String posfijo, String lexema) {
        Stack<String> stack = new Stack<>();
        List<Cuadruplo> cuadruplos = new ArrayList<>();
        String[] tokens = posfijo.split(",");
        int tempCounter = 1;


        for (String token : tokens) {
            if (token.matches("[\\dA-Za-z]+(\\.\\d+)?")) {  // Si el token es un operando, convertirlo a entero y apilarlo
                stack.push(token);
            }  else {  // Si el token es un operador
                String b = stack.pop();
                String a = stack.pop();

                if (isNumeric(a) && isNumeric(b)) {
                    int aValue = Integer.parseInt(a);
                    int bValue = Integer.parseInt(b);
                    int result = 0;

                    switch (token) {
                        case "+":
                            result = aValue + bValue;
                            break;
                        case "-":
                            result = aValue - bValue;
                            break;
                        case "*":
                            result = aValue * bValue;
                            break;
                        case "/":
                            result = aValue / bValue;  // División entera
                            break;
                    }

                    stack.push(String.valueOf(result)) ;
                } else if (isReal(a) && isReal(b)) {
                    Float aValue = Float.parseFloat(a);
                    Float bValue = Float.parseFloat(b);
                    Float result = (float) 0;

                    switch (token) {
                        case "+":
                            result = aValue + bValue;
                            break;
                        case "-":
                            result = aValue - bValue;
                            break;
                        case "*":
                            result = aValue * bValue;
                            break;
                        case "/":
                            result = aValue / bValue;  // División entera
                            break;
                    }

                    stack.push(String.valueOf(result)) ;
                }

                else {
                    String tempVar = "t" + Intermedio.getContVarTemp();
                    cuadruplos.add(new Cuadruplo(token, a, b, tempVar));
                    stack.push(tempVar);
                }

            }
        }
        cuadruplos.add(new Cuadruplo("Assign", stack.pop()+"", "", lexema));
        return cuadruplos;
    }

    private static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isReal(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


}
