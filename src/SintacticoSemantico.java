import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SintacticoSemantico {

    private String lexema;
    private int token;
    private Nodo nodo;
    private static int renglon;
    private int fin = 0;
    private int parentesis = 0;
    private static ArrayList<TreeNode> listaIntermedio;
    private static Boolean condicionValida = true;

    public static SymbolTable symbolTable;
    private String tipoDato;

    public static ArrayList<TreeNode> getListaIntermedio() {
        return listaIntermedio;
    }


    public SintacticoSemantico(Nodo nodo) {
        listaIntermedio = new ArrayList<>();

        this.nodo = nodo;
        lexema = nodo.lexema;
        token = nodo.token;
        renglon = nodo.renglon;

        symbolTable = new SymbolTable();
        program();
    }

    private void getError(String error) {

        switch (error)
        {
            case ".":
                System.out.println("Se esperaba '.' al final del programa ");
                break;
            case "cierre":
                System.out.println("Se esperaba cierre de línea. (Renglón " + renglon + ")");
                break;
            case "numAsig":
                System.out.println("No se puede asignar un valor a un número. (Renglón " + renglon + ")");
                break;
            case "program":
                System.out.println("Se esperaba iniciar con 'program' (Renglón " + renglon + ")");
                break;
            case "end":
                System.out.println("Se esperaba finalizar un end " + fin);
                break;
            case "identificador":
                System.out.println("Se esperaba un identificador (Renglón " + renglon + ")");
                break;
            case "begin":
                System.out.println("Se esperaba un begin (Renglón " + renglon + ")");
                break;
            case "tipoDato":
                System.out.println("Se esperaba tipo de dato (Renglón " + renglon + ")");
                break;
            case ":":
                System.out.println("Se esperaba ':' (Renglón " + renglon + ")");
                break;
            case "do":
                System.out.println("Se esperaba 'do' (Renglón " + renglon + ")");
                break;
            case "numLetra":
                System.out.println("Se esperaba un número o letra (Renglón " + renglon + ")");
                break;
            case "aritmetico":
                System.out.println("Se esperaba un operador aritmético (Renglón " + renglon + ")");
                break;
            case "asignacion":
                System.out.println("Se esperaba un operador de asignación (Renglón " + renglon + ")");
                break;
            case "then":
                System.out.println("Se esperaba 'then' (Renglón " + renglon + ")");
                break;
            case "relacional":
                System.out.println("Se esperaba un operador relacional (Renglón " + renglon + ")");
                break;
            case "condicion":
                System.out.println("Se esperaba una condición. (Renglón " + renglon + ")");
                break;

            default:
                System.out.println("Hay un error.");
        }

        //System.exit(0);
    }

    private void nuevoToken() {
        nodo = nodo.vaciar;
        if (nodo != null) {
            lexema = nodo.lexema;
            token = nodo.token;
            renglon = nodo.renglon;
        }
    }

    public static int getRenglon() {
        return renglon;
    }

    private void program() {
        if (token == 200) {
            agregarTablaSimbolos(token);
            identificador();
            if (token == 118) {
                //agregarTablaSimbolos(token);
                bloque();
                if (nodo == null && token != 116) {
                    getError(".");
                }
                if (nodo == null && token == 116) {
                    if (fin != 0) getError("end");
                    else {
                        System.out.println("Perfecto\n");

                        return;
                    }
                }
                if (nodo != null) {
                    nuevoToken();
                    if (token == 116) {
                        if (fin != 0) getError("end");
                        else {
                            agregarTablaSimbolos(token);
                            System.out.println("Perfecto\n");
                            renglon = nodo.renglon;
                            return;
                        }
                    } else {
                        getError(".");
                    }
                }
            } else {
                getError("cierre");

            }
        } else {
            getError("program");

        }
    }

    private void identificador() {
        nuevoToken();
        if (token == 100) {
            symbolTable.addEntry(lexema, "Identificador", token, renglon);
            nuevoToken();
            if (token == 117) {
                listaIdentificador();
            }
        } else {
            getError("identificador");
        }
    }

    private void listaIdentificadorDeclaraciones(TreeNode declaraciones) {
        nuevoToken();

        if (token == 100) {
            declaraciones.addChild(new TreeNode(lexema, lexema));

            nuevoToken();
            if (token == 117) {

                listaIdentificadorDeclaraciones(declaraciones);
            }
        } else {
            getError("identificador");
        }
    }

    private void listaIdentificador() {
        nuevoToken();
        if (token == 100) {
            agregarTablaSimbolos(token);
            nuevoToken();
            if (token == 117) {
                //agregarTablaSimbolos(token);
                listaIdentificador();
            }
        } else {
            getError("identificador");
        }
    }

    private void bloque() {
        nuevoToken();
        if (nodo != null) {
            while (token == 221) {
                if (token == 221) {
                    agregarTablaSimbolos(token);
                    definiciones();
                }
            }


            if (token == 205) {

                bloqueEnunciados();
            } else {
                getError("begin");
            }
        }
    }

    private static void printTree(TreeNode node, String indent) {
        if (node != null) {
            //System.out.println(indent + node.value);
            for (TreeNode child : node.children) {
                printTree(child, indent + "  ");
            }
        }
    }

    private void definiciones() {
        TreeNode declaraciones = new TreeNode("declaraciones", lexema);
        
        listaIdentificadorDeclaraciones(declaraciones);
        listaIntermedio.add(declaraciones);

        printTree(declaraciones, "");

        if (token == 119) {
            agregarTablaSimbolos(token);
            nuevoToken();
            if (token == 202) {
                tipoDato = "Int";
                agregarDeclaracion(declaraciones);
                agregarTablaSimbolos(token);

                nuevoToken();
                if (token == 118) {
                    //agregarTablaSimbolos(token);
                    nuevoToken();
                } else {
                    getError("cierre");
                }
            } else if (token == 203) {
                tipoDato = "Real";
                agregarDeclaracion(declaraciones);
                agregarTablaSimbolos(token);

                nuevoToken();

                if (token == 118) {
                    //agregarTablaSimbolos(token);
                    nuevoToken();
                } else {
                    getError("cierre");
                }
            } else if (token == 201) {
                tipoDato = "Character";
                agregarDeclaracion(declaraciones);
                agregarTablaSimbolos(token);

                nuevoToken();

                if (token == 118) {
                    //agregarTablaSimbolos(token);
                    nuevoToken();
                } else {
                    getError("cierre");
                }
            } else if (token == 204) {
                tipoDato = "Boolean";
                agregarDeclaracion(declaraciones);
                agregarTablaSimbolos(token);

                nuevoToken();

                if (token == 118) {
                    //agregarTablaSimbolos(token);
                    nuevoToken();
                } else {
                    getError("cierre");
                }
            }

            else {
                getError("tipoDato");
            }
        } else {
            getError(":");
        }
    }

    private void listaEnunciados() {
        if (nodo == null) {
            return;
        }

        nuevoToken();
        if (token == 100) {
            while (token == 100) {
                TreeNode asignacion = new TreeNode(lexema, lexema, token);
                //agregarTablaSimbolos(token);
                nuevoToken();
                operaciones(asignacion);

                if (parentesis!=0) {
                    if (parentesis < 0) {
                        System.out.println("Te falto abrir un paréntesis");
                    } else {
                        System.out.println("Te falto cerrar un paréntesis.");
                    }

                    System.exit(0);
                }

                agregarDefTabla(asignacion);

                listaIntermedio.add(asignacion);

                printTree(asignacion, "");
            }
        }

        if (token == 206) {
            agregarTablaSimbolos(token);
            fin--;
            return;
        }

        if (token == 209) {
            agregarTablaSimbolos(token);
            alternativas();
            nuevoToken();
            if (token == 206) {
                agregarTablaSimbolos(token);
                fin--;
                listaIntermedio.add(new TreeNode("End if", "End if"));
                return;
            }
            if (token == 211) {
                agregarTablaSimbolos(token);
                alternativasElse();
                listaIntermedio.add(new TreeNode("End if", "End if"));
            }
        }

        if (token == 212) {
            agregarTablaSimbolos(token);
            TreeNode condiciones = new TreeNode("condiciones", "while");
            nuevoToken();
            condicion(condiciones);
            if (token == 213) {
                agregarTablaSimbolos(token);

                if(condicionValida || true) {
                    bloque();
                    listaIntermedio.add(new TreeNode("End if", "End if"));
                } else {
//                    int contBegin = 0;
//                    do {
//                        nuevoToken();
//                        if (token == 205) contBegin++;
//                        if (token == 206) contBegin--;
//
//                        if (contBegin == 0) {
//                            break;
//                        }
//                    } while (nodo != null);
                }
            } else {
                getError("do");
            }
        }

        otro();

        listaEnunciados();
    }

    private void otro() {
        if (token == 101 || token == 102) {
            getError("numAsig");
            System.exit(0);
        }
        if (isOperador(token)) {
            getError("identificador");
            System.exit(0);
        }
    }

    private boolean isOperador(int token) {
        Integer[] tokensOperadores = {103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 124};

        for (int i = 0; i < tokensOperadores.length; i++) {
            if (token == tokensOperadores[i]) {
                return true;
            }
        }
        return false;
    }

    private void operaciones(TreeNode nodo) {

        if (token == 108 || token == 109 || token == 110 || token == 111 || token == 112 || token == 113) {
            agregarTablaSimbolos(token);

            nuevoToken();
            if (token == 100 || token == 101 || token == 102) {
                agregarTablaSimbolos(token);
                nuevoToken();
                if (token == 103 || token == 104 || token == 105 || token == 106 || token == 107) {
                    agregarTablaSimbolos(token);
                    nuevoToken();
                    if (token == 100 || token == 101 || token == 102) {
                        agregarTablaSimbolos(token);
                        nuevoToken();
                        if (token == 118) {
                            //agregarTablaSimbolos(token);
                            nuevoToken();
                        } else {
                            getError("cierre");
                        }
                    } else {
                        getError("numLetra");
                    }
                } else {
                    getError("aritmetico");
                }
            } else {
                getError("numLetra");
            }
        } else if (token == 124) {
            //agregarTablaSimbolos(token);
            nodo.addChild(new TreeNode(lexema, lexema, token));
            nuevoToken();

            while (token == 114) {
                nodo.addChild(new TreeNode(lexema, lexema, token));
                parentesis++;
                nuevoToken();
            }

            if (token == 100 || token == 101 || token == 102) {
                nodo.addChild(new TreeNode(lexema, lexema, token));
                //agregarTablaSimbolos(token); // Aqui se agrega a la tabla de simbolos junto con su tipo de dato.
                nuevoToken();

                while (token == 115) {
                    nodo.addChild(new TreeNode(lexema, lexema, token));
                    parentesis--;
                    nuevoToken();
                }

                if (token == 118) {
                    //agregarTablaSimbolos(token);
                    nuevoToken();
                } else if (token == 103 || token == 104 || token == 105 || token == 106 || token == 107) {
                    //agregarTablaSimbolos(token);
                    nodo.addChild(new TreeNode(lexema, lexema, token));

                    nuevoToken();
                    if (token == 118) {
                        getError("numLetra");
                    }
                    while (token != 118) {

                        while (token == 114) {
                            nodo.addChild(new TreeNode(lexema, lexema, token));
                            parentesis++;
                            nuevoToken();
                        }

                        if (token == 100 || token == 101 || token == 102) {

                            //agregarTablaSimbolos(token);
                            nodo.addChild(new TreeNode(lexema, lexema, token));

                            nuevoToken();

                            while (token == 115) {
                                nodo.addChild(new TreeNode(lexema, lexema, token));
                                parentesis--;
                                nuevoToken();
                            }

                            if (token == 118) {
                                //agregarTablaSimbolos(token);
                                break;
                            }
                            if (token == 103 || token == 104 || token == 105 || token == 106 || token == 107) {
                                //agregarTablaSimbolos(token);
                                nodo.addChild(new TreeNode(lexema, lexema, token));
                                nuevoToken();
                                if (token == 118) {
                                    getError("numLetra");
                                }
                            } else {
                                if (token == 100 || token == 101 || token == 102) {
                                    getError("aritmetico");
                                } else {
                                    getError("cierre");
                                }
                                break;
                            }
                        } else {
                            getError("numLetra");
                        }
                    }

                } else {
                    getError("cierre");
                }
            } else {
                getError("numLetra");
            }
        } else {
            getError("asignacion");
        }
    }

    private void alternativas() {
        nuevoToken();
        TreeNode condiciones = new TreeNode("condiciones", "if");
        condicion(condiciones);
        //System.out.println(lexema + " <--");
        if (token == 210) {
            agregarTablaSimbolos(token);

            if(condicionValida || true) {
                nuevoToken();
                bloqueEnunciados();


            } else {
//                int contBegin = 0;
//                do {
//                    nuevoToken();
//                    if (token == 205) contBegin++;
//                    if (token == 206) contBegin--;
//                    //System.out.println(token + " <- token bloque condicion");
//                    if (contBegin == 0) {
//                        break;
//                    }
//                } while (nodo != null);
            }
        } else {
            getError("then");
        }
    }

    private void alternativasElse() {
        if(!condicionValida || true) {
            listaIntermedio.add(new TreeNode("Else", "else"));
            nuevoToken();
            bloqueEnunciados();
            listaIntermedio.add(new TreeNode("Else End", "else end"));
        } else {
//            int contBegin = 0;
//            do {
//                nuevoToken();
//                if (token == 205) contBegin++;
//                if (token == 206) contBegin--;
//
//                if (contBegin == 0) {
//                    break;
//                }
//            } while (nodo != null);
        }
    }

    private void bloqueEnunciados() {
        if (token == 205) {
            agregarTablaSimbolos(token);
            fin++;
            listaEnunciados();
        } else {
            getError("begin");
        }
    }

    private void condicion(TreeNode condiciones) {

        if (token == 100 || token == 101 || token == 102) {
            agregarTablaSimbolos(token);

            condiciones.addChild(new TreeNode(lexema, lexema, token));

            nuevoToken();
            if (token == 108 || token == 109 || token == 110 || token == 111 || token == 112 || token == 113) {
                agregarTablaSimbolos(token);
                condiciones.addChild(new TreeNode(lexema, lexema, token));

                nuevoToken();
                if (token == 100 || token == 101 || token == 102) {
                    agregarTablaSimbolos(token);
                    condiciones.addChild(new TreeNode(lexema, lexema, token));

                    mismoTipo(condiciones);
                    //System.out.println(token + " <- " + lexema);
                    //          condicionValida(condiciones);
                    //        System.out.println(condicionValida.toString() + " <-- Resultado de condición");
                    listaIntermedio.add(condiciones);
                    printTree(condiciones, "");
                    nuevoToken();
                } else if (token == 222 || token == 223) {
                    agregarTablaSimbolos(token);
                    condiciones.addChild(new TreeNode(lexema, lexema));

                    mismoTipo(condiciones);

                    listaIntermedio.add(condiciones);
                    //System.out.println(condicionesLista + " <- COndiciones Lista");
                    printTree(condiciones, "");
                    nuevoToken();
                } else {
                    getError("numLetra");
                }
            } else {
                getError("relacional");
            }
        } else {
            getError("condicion");
        }
    }

    // Clases semántico

    private void mismoTipo(TreeNode nodo) {
        if  (nodo != null) {

            String prmLexema = nodo.children.get(0).lexema;
            String operador = nodo.children.get(1).lexema;
            String segLexema = nodo.children.get(2).lexema;
            String bloque = nodo.lexema;

            String primerTipo = symbolTable.getEntry(prmLexema).type;
            String segundoTipo = symbolTable.getEntry(segLexema).type;

            //System.out.println("Condicion -> " + prmLexema + " " + primerTipo);
            //System.out.println("Condicion -> " + segLexema + " " + segundoTipo);

            if (!primerTipo.equalsIgnoreCase(segundoTipo)) {
                System.out.println("No son del mismo tipo: '" + prmLexema + "' y '" + segLexema + "'" );
                System.exit(0);
            }

            if (primerTipo.equalsIgnoreCase("Boolean")) {
                if (!(operador.equals("==") || operador.equals("!="))) {
                    System.out.println("No puedes usar este tipo de operador para Boolean");
                    System.exit(0);
                }
            }

            if (bloque.equalsIgnoreCase("while")) {
                if (nodo.children.get(0).token == 101 || nodo.children.get(0).token == 102) {
                    if (nodo.children.get(2).token == 101 || nodo.children.get(2).token == 102) {
                        System.out.println("No puedes usar dos constantes en la condición, se hará un bucle infinito. (Renglón " + renglon + ")");
                        System.exit(0);
                    }
                }
            }
        }
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    private void agregarDefTabla(TreeNode nodo) {

        String lexema = nodo.lexema;
        List<TreeNode> valorNodo = nodo.children;
        List<String> valores = Collections.singletonList(nodo.children.toString());

        if (!symbolTable.contains(nodo.lexema)) {
            System.out.println(nodo.lexema + " no está declarada. (Renglón " + renglon + ")");
            System.exit(0);
        }
        String type = symbolTable.getEntry(nodo.lexema).type;

        if (nodo.token == 100) {

            if (symbolTable.getEntry(nodo.lexema).type.equalsIgnoreCase("Palabra Reservada")) {
                System.out.println("No puedes usar el nombre del programa");
                System.exit(0);
            }
        }

        if (valores.get(0).equals("=")) {
            valores.remove(0);
        }

        // Construir la expresión concatenada
        StringBuilder resultado = new StringBuilder();
        for (String val : valores) {
            resultado.append(val).append(" ");
        }
        String rFinal = resultado.toString().replaceAll("[,=\\[\\]]", "").trim();

        // Eliminar el último espacio en blanco
        if (resultado.length() > 0) {
            resultado.setLength(resultado.length() - 1);
        }

        String valor = "";
        int tokenValor = 0;
        String valorPrev = "";
        int tokenValorPrev = 0;

        for (int i = 0; i < nodo.children.size(); i++) {
            if (nodo.children.get(i).token == 100 || nodo.children.get(i).token == 101 || nodo.children.get(i).token == 102) {
                // Si el valor es vacío, inicializarlo
                if (valor.equals("")) {
                    valor = nodo.children.get(i).lexema;
                    tokenValor = nodo.children.get(i).token;

                    if (!(symbolTable.contains(valor)) && tokenValor == 100) {
                        System.out.println(valor + " no está declarado. (Renglón " + renglon + ")");
                        System.exit(0);
                    }

                    if (tokenValor == 100) {
                        if (symbolTable.getEntry(valor).type.equalsIgnoreCase("Palabra Reservada")) {
                            System.out.println("No puedes usar el nombre del programa");
                            System.exit(0);
                        }
                    }

                    // Comprobar si la misma variable se asigna a sí misma
                    if (valor.equals(lexema) && nodo.children.size() == 2) {
                        System.out.println("No puedes asignar la misma variable");
                        System.exit(0);
                    }

                    if ((tokenValor == 101 && !type.equals("Int")) || (tokenValor == 102 && !type.equals("Real"))) {
                        System.out.println("No se puede asignar un tipo diferente de dato (Renglón " + (renglon - 1) + ")");
                        System.exit(0);
                    }

                    if (tokenValor == 100) {
                        String tipoEntrada = symbolTable.getEntry(valor).type;
                        if (!type.equals(tipoEntrada)) {
                            System.out.println("No se puede asignar un tipo de dato diferente a " + lexema + ". (Renglón " + (renglon - 1) + ")");
                            System.exit(0);
                        }
                    }

                } else {
                    // Actualizar el valor previo y el valor actual
                    valorPrev = valor;
                    tokenValorPrev = tokenValor;
                    valor = nodo.children.get(i).lexema;
                    tokenValor = nodo.children.get(i).token;


                    // Verificar si el lexema está en la tabla de símbolos
                    if (!symbolTable.contains(valor) && nodo.children.get(i).token == 100) {
                        System.out.println(valor + " no está declarado. (Renglón " + renglon + ")");
                        System.exit(0);
                    }

                    if (!symbolTable.contains(valorPrev) && tokenValorPrev == 100) {
                        System.out.println(valorPrev + " no está declarado. (Renglón " + renglon + ") k");
                        System.exit(0);
                    }

                    if (tokenValor == 100 || tokenValorPrev == 100) {

                        if (tokenValor == 100) {
                            if (symbolTable.getEntry(valor).type.equalsIgnoreCase("Palabra Reservada")) {
                                System.out.println("No puedes usar el nombre del programa");
                                System.exit(0);
                            }
                        } else if (tokenValorPrev == 100 ) {

                            if (symbolTable.getEntry(valorPrev).type.equalsIgnoreCase("Palabra Reservada")) {
                                System.out.println("No puedes usar el nombre del programa ");
                                System.exit(0);
                            }
                        }

                    }

                    if (tokenValor == 101 || tokenValorPrev == 101) {
                        String tipo = "Int";
                        if (!type.equals(tipo)) {
                            System.out.println("No se puede asignar un tipo diferente de dato (Renglón " + renglon + ")");
                            System.exit(0);
                        }

                        if (tokenValor == 101 && tokenValorPrev == 100) {
                            if (!(symbolTable.getEntry(valorPrev).type.equals(tipo))) {
                                System.out.println("No son del mismo tipo: '" + valor + "' y '" + valorPrev + "' (Renglón " + renglon + ")" );
                                System.exit(0);
                            }
                        } else if (tokenValor == 100 && tokenValorPrev == 101) {
                            if (!(symbolTable.getEntry(valor).type.equals(tipo))) {
                                System.out.println("No son del mismo tipo: '" + valor + "' y '" + valorPrev + "' (Renglón " + renglon + ")" );
                                System.exit(0);
                            }
                        }

                    } else if (tokenValor == 102 || tokenValorPrev == 102) {
                        String tipo = "Real";
                        if (!type.equals(tipo)) {
                            System.out.println("No se puede asignar un tipo diferente de dato (Renglón " + renglon + ")");
                            System.exit(0);
                        }

                        if (tokenValor == 102 && tokenValorPrev == 100) {
                            if (!(symbolTable.getEntry(valorPrev).type.equals(tipo))) {
                                System.out.println("No son del mismo tipo: '" + valor + "' y '" + valorPrev + "' (Renglón " + renglon + ")" );
                                System.exit(0);
                            }
                        } else if (tokenValor == 100 && tokenValorPrev == 102) {
                            if (!(symbolTable.getEntry(valor).type.equals(tipo))) {
                                System.out.println("No son del mismo tipo: '" + valor + "' y '" + valorPrev + "' (Renglón " + renglon + ")" );
                                System.exit(0);
                            }
                        }
                    } else if (tokenValor == 100 && tokenValorPrev == 100) {

                        if (!(symbolTable.getEntry(valor).type.equals(symbolTable.getEntry(valorPrev).type))) {
                            System.out.println("No son del mismo tipo: '" + valor + "' y '" + valorPrev + "' (Renglón " + renglon + ")" );
                            System.exit(0);
                        }
                    }
                }


            }
        }

        symbolTable.addEntry(lexema, type, nodo.token, renglon, rFinal);



        for (int i = 0; i < nodo.children.size(); i++) {
            agregarTablaSimbolos(nodo.children.get(i).token, nodo.children.get(i).lexema);
        }
    }

    private void agregarTablaSimbolos(int token, String lexema) {

        if (!symbolTable.contains(lexema) && (token == 100)) {
            System.out.println(lexema + " no está declarado. (Renglón " + renglon + ")");
            System.exit(0);
        }

        if (token == 100) {
            if (symbolTable.getEntry(lexema).type.equalsIgnoreCase("Palabra Reservada")) {
                System.out.println("No puedes usar el nombre del programa");
                System.exit(0);
            }
            tipoDato = symbolTable.getEntry(lexema).type;
        } else if (token == 101) {
            tipoDato = "Int";
        } else if (token == 102) {
            tipoDato = "Real";
        } else if (isOperador(token)) {
            tipoDato = "Operador";
        } else if (token == 114 || token == 115 || token == 116 || token == 117 || token == 118) {
            tipoDato = "Simbolo";
        } else if (token == 119) {
            tipoDato = "Asignación";
        } else if (token == 222 || token == 223) {
            tipoDato = "Boolean";
        } else if (token >= 200) {
            tipoDato = "Palabra reservada";
        }
        else {
            tipoDato = "Undefined";
        }

        symbolTable.addEntry(lexema, tipoDato, token, renglon);
    }

    private void agregarTablaSimbolos(int token) {

        if (!condicionValida) {
            //System.out.println("aaa ");
            //return;
        }

        if (!symbolTable.contains(lexema) && (token == 100)) {
            System.out.println(lexema + " no está declarado. (Renglón " + renglon + ")");
            System.exit(0);
        }

        if (token == 100) {
            if (symbolTable.getEntry(lexema).type.equalsIgnoreCase("Palabra Reservada")) {
                System.out.println("No puedes usar el nombre del programa");
                System.exit(0);
            }
            tipoDato = symbolTable.getEntry(lexema).type;
        } else if (token == 101) {
            tipoDato = "Int";
        } else if (token == 102) {
            tipoDato = "Real";
        } else if (isOperador(token)) {
            tipoDato = "Operador";
        } else if (token == 114 || token == 115 || token == 116 || token == 117 || token == 118) {
            tipoDato = "Simbolo";
        } else if (token == 119) {
            tipoDato = "Asignación";
        } else if (token == 222 || token == 223) {
            tipoDato = "Boolean";
        } else if (token >= 200) {
            tipoDato = "Palabra reservada";
        }
        else {
            tipoDato = "Undefined";
        }

        symbolTable.addEntry(lexema, tipoDato, token, renglon);
    }

    private void agregarDeclaracion(TreeNode nodo) {
        if  (nodo != null) {
            for (TreeNode child: nodo.children) {
                if (symbolTable.contains(child.lexema)) {
                    System.out.println(child.lexema + " ya está declarado. (Renglón " + renglon + ")");
                    System.exit(0);
                }
                symbolTable.addEntry(child.lexema, tipoDato, token, renglon);
            }
        }
    }

    private void condicionValida(TreeNode nodo) {

        String param1 = "";
        int tipoParam1 = 0;
        String param2 = "";
        int tipoParam2 = 0;
        String operador = "";

        if (nodo != null) {
            for (TreeNode child : nodo.children) {
                //System.out.println(child.lexema + " <--");
                if (param1.equals("")) {
                    param1 = child.lexema;

                    tipoParam1 = child.token;
                    //System.out.println(child.token + " <- Token");
                }
                else if (operador.equals("")) operador = child.lexema;
                else if (param2.equals("")) {
                    param2 = child.lexema;
                    tipoParam2 = child.token;
                }
            }


            if (operador.equals("<") || operador.equals("< ")) {

                if (tipoParam1 == 101) {
                    int valorParam1 = Integer.parseInt(param1);
                    int valorParam2 = Integer.parseInt(param2);

                    condicionValida = valorParam1 < valorParam2;
                } else if (tipoParam1 == 102) {
                    float valorParam1 = Float.parseFloat(param1);
                    float valorParam2 = Float.parseFloat(param2);
                    condicionValida = valorParam1 < valorParam2;
                } else {
                    System.out.println("Tipo de valores diferentes.");
                    System.exit(0);
                }

            } else if (operador.equals(">")) {

                //System.out.println(tipoParam1);
                if (tipoParam1 == 101) {
                    int valorParam1 = Integer.parseInt(param1);
                    int valorParam2 = Integer.parseInt(param2);

                    condicionValida = valorParam1 > valorParam2;
                } else if (tipoParam1 == 102) {
                    float valorParam1 = Float.parseFloat(param1);
                    float valorParam2 = Float.parseFloat(param2);

                    condicionValida = valorParam1 > valorParam2;
                } else {
                    System.out.println("Tipo de valores diferentes.");
                    System.exit(0);
                }

            } else if (operador.equals("<=")) {

                if (tipoParam1 == 101) {
                    int valorParam1 = Integer.parseInt(param1);
                    int valorParam2 = Integer.parseInt(param2);

                    condicionValida = valorParam1 <= valorParam2;
                } else if (tipoParam1 == 102) {
                    float valorParam1 = Float.parseFloat(param1);
                    float valorParam2 = Float.parseFloat(param2);

                    condicionValida = valorParam1 <= valorParam2;
                } else {
                    System.out.println("Tipo de valores diferentes.");
                    System.exit(0);
                }

            } else if (operador.equals(">=")) {

                if (tipoParam1 == 101) {
                    int valorParam1 = Integer.parseInt(param1);
                    int valorParam2 = Integer.parseInt(param2);

                    condicionValida = valorParam1 >= valorParam2;
                } else if (tipoParam1 == 102) {
                    float valorParam1 = Float.parseFloat(param1);
                    float valorParam2 = Float.parseFloat(param2);

                    condicionValida = valorParam1 >= valorParam2;
                } else {
                    System.out.println("Tipo de valores diferentes.");
                    System.exit(0);
                }

            } else if (operador.equals("!=")) {

                if (tipoParam1 == 101) {
                    int valorParam1 = Integer.parseInt(param1);
                    int valorParam2 = Integer.parseInt(param2);

                    condicionValida = valorParam1 != valorParam2;
                } else if (tipoParam1 == 102) {
                    float valorParam1 = Float.parseFloat(param1);
                    float valorParam2 = Float.parseFloat(param2);

                    condicionValida = valorParam1 != valorParam2;
                } else {
                    System.out.println("Tipo de valores diferentes.");
                    System.exit(0);
                }

            } else if (operador.equals("==")) {

                if (tipoParam1 == 101) {
                    int valorParam1 = Integer.parseInt(param1);
                    int valorParam2 = Integer.parseInt(param2);

                    condicionValida = valorParam1 == valorParam2;
                } else if (tipoParam1 == 102) {
                    float valorParam1 = Float.parseFloat(param1);
                    float valorParam2 = Float.parseFloat(param2);

                    condicionValida = valorParam1 == valorParam2;
                } else {
                    System.out.println("Tipo de valores diferentes.");
                    System.exit(0);
                }

            }
        }
    }

}
