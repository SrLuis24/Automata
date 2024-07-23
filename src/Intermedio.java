import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Intermedio {

    private SymbolTable tablaSimbolos;
    private ArrayList<TreeNode> listaIntermedio = SintacticoSemantico.getListaIntermedio();
    public static List<Cuadruplo> cuadruplosIntermedio = new ArrayList<>();
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
                Cuadruplo goTo = new Cuadruplo("GOTO", " ", " ", "L"+(getContLabelTemp()));
                cuadruplosIntermedio.add(goTo);

                int labelAnterior = labels.size() - 2;
                Cuadruplo label = new Cuadruplo("L" + labels.get(labelAnterior), " ", " ", " ");
                cuadruplosIntermedio.add(label);
                labels.remove(labelAnterior);
                continue;
            }

            if (nodo.value.equalsIgnoreCase("else end")) {
                continue;
            }
            if (nodo.value.equalsIgnoreCase("end if")) {
                Cuadruplo label = new Cuadruplo("L" + labels.pop(), " ", " ", " ");
                cuadruplosIntermedio.add(label);
                continue;
            }

            InfijoPosfijo expresion = new InfijoPosfijo(nodo);

            String tipo = SintacticoSemantico.symbolTable.getEntry(nodo.lexema).type;

            String posfijo = expresion.getExpresionPosfija().toString();
            List<Cuadruplo> cuadruplos;

            if (true) { // Para usar el optimizado usa true y false para sin optimizar
                cuadruplos = PosfijoACuadruplosOptimizado.crearCuadruplosOptimizado(posfijo, nodo.lexema);
            } else {
                cuadruplos = PosfijoACuadruplos.crearCuadruplos(posfijo, tipo);
                cuadruplos.add(new Cuadruplo("Assig", "t" + getActualVarTemp(), " ", nodo.value));
            }
            cuadruplos.forEach(c -> cuadruplosIntermedio.add(c));

        }

        imprimirCuadruplos();
        System.out.println("\n");

        /* GENERADOR DE CÓDIGO OBJETO */

        /*GeneradorObjeto codeGenerator = new GeneradorObjeto(cuadruplosIntermedio);
        String asmCode = codeGenerator.generate();
        System.out.println(asmCode);

        try {
            codeGenerator.writeToFile("objeto.asm");
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        GeneradorObjeto2.determineVariableTypes(cuadruplosIntermedio);
        String codeEnsamblador = GeneradorObjeto2.generateAssembly(cuadruplosIntermedio);
        System.out.println(codeEnsamblador);
        GeneradorObjeto2.saveToFile("objeto.asm", codeEnsamblador);


    }

    private void imprimirCuadruplos() {
        cuadruplosIntermedio.forEach(c -> System.out.println(c));
    }

    private void imprimirDeclaracion(TreeNode declaracion) {
            for (TreeNode b : declaracion.children) {
                Cuadruplo c = new Cuadruplo(tablaSimbolos.getEntry(b.lexema).type, b.lexema, "", null);
                cuadruplosIntermedio.add(c);
            }
    }

    private void imprimirCondicion(TreeNode condicion) {

            String op1 = condicion.children.get(0).lexema;
            String operador = condicion.children.get(1).lexema;
            String op2 = condicion.children.get(2).lexema;

            String varTemp = "t" + getContVarTemp();
            Cuadruplo c = new Cuadruplo(operador, op1, op2, varTemp);
            cuadruplosIntermedio.add(c);
            Cuadruplo d = new Cuadruplo("IF_FALSE", varTemp, " ", "L" + getContLabelTemp());
            cuadruplosIntermedio.add(d);

    }


}
