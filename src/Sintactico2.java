
public class Sintactico2 {

    private String lexema;
    private int token;
    private Nodo nodo;
    private static int renglon;
    private int fin = 0;

    private SymbolTable symbolTable;
    private String tipoDato;


    public Sintactico2(Nodo nodo) {

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
                System.out.println("Se esperaba '.' al final del programa");
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
                System.out.println("Se esperaba finalizar un end");
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
            default:
                System.out.println("Hay un error.");
        }

        System.exit(0);
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
                        System.out.println("Perfecto");
                        System.out.println(lexema);
                        return;
                    }
                }
                if (nodo != null) {
                    nuevoToken();
                    if (token == 116) {
                        if (fin != 0) getError("end");
                        else {
                            agregarTablaSimbolos(token);
                            System.out.println("Perfecto");
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
            symbolTable.addEntry(lexema, "Reservada", 0, renglon);
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

    private void printTree(TreeNode node, String indent) {
        if (node != null) {
            //System.out.println(indent + node.value);
            for (TreeNode child : node.children) {
                printTree(child, indent + "  ");
            }
        }
    }

    private void agregarDeclaracion(TreeNode nodo) {
        if  (nodo != null) {
            for (TreeNode child: nodo.children) {
                if (symbolTable.contains(child.lexema)) {
                    System.out.println(child.lexema + " ya está declarado. (Renglón " + renglon + ")");
                    System.exit(0);
                }
                symbolTable.addEntry(child.lexema, tipoDato, 0, renglon);
            }
        }
    }

    private void definiciones() {
        TreeNode declaraciones = new TreeNode("declaraciones", lexema);
        
        listaIdentificadorDeclaraciones(declaraciones);

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
                agregarTablaSimbolos(token);
                nuevoToken();
                operaciones();
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
            if (token == 211) {
                agregarTablaSimbolos(token);
                alternativasElse();
            }
        }

        if (token == 212) {
            agregarTablaSimbolos(token);

            nuevoToken();
            condicion();
            if (token == 213) {
                agregarTablaSimbolos(token);
                bloque();
            } else {
                getError("do");
            }
        }

        otro();

        listaEnunciados();
    }

    private void otro() {
        if (token == 101 || token == 102) {
            System.out.println(lexema);
            getError("numAsig");
            System.exit(0);
        }
        if (isOperador(token)) {
            getError("identificador");
            System.exit(0);
        }
    }

    private void agregarTablaSimbolos(int token) {

        if (!symbolTable.contains(lexema) && (token == 100)) {
            System.out.println(lexema + " no está declarado. (Renglón " + renglon + ")");
            System.exit(0);
        }

        if (token == 100) {
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
            tipoDato = "No se";
        }

        symbolTable.addEntry(lexema, tipoDato, 0, renglon);
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

    private void operaciones() {

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
            agregarTablaSimbolos(token);
            nuevoToken();

            if (token == 100 || token == 101 || token == 102) {
                agregarTablaSimbolos(token); // Aqui se agrega a la tabla de simbolos junto con su tipo de dato.
                nuevoToken();
                if (token == 118) {
                    //agregarTablaSimbolos(token);
                    nuevoToken();
                } else if (token == 103 || token == 104 || token == 105 || token == 106 || token == 107) {
                    agregarTablaSimbolos(token);

                    nuevoToken();
                    if (token == 118) {
                        getError("numLetra");
                    }
                    while (token != 118) {
                        if (token == 100 || token == 101 || token == 102) {

                            agregarTablaSimbolos(token);

                            nuevoToken();
                            if (token == 118) {
                                //agregarTablaSimbolos(token);
                                break;
                            }
                            if (token == 103 || token == 104 || token == 105 || token == 106 || token == 107) {
                                agregarTablaSimbolos(token);
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
        condicion();
        if (token == 210) {
            agregarTablaSimbolos(token);
            nuevoToken();
            bloqueEnunciados();
        } else {
            getError("then");
        }
    }

    private void alternativasElse() {
        nuevoToken();
        bloqueEnunciados();
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

    private void condicion() {
        TreeNode condiciones = new TreeNode("condiciones", "");
        if (token == 100 || token == 101 || token == 102) {
            agregarTablaSimbolos(token);

            condiciones.addChild(new TreeNode(lexema, lexema));

            nuevoToken();
            if (token == 108 || token == 109 || token == 110 || token == 111 || token == 112 || token == 113) {
                agregarTablaSimbolos(token);
                condiciones.addChild(new TreeNode(lexema, lexema));

                nuevoToken();
                if (token == 100 || token == 101 || token == 102) {
                    agregarTablaSimbolos(token);
                    condiciones.addChild(new TreeNode(lexema, lexema));

                    mismoTipo(condiciones);

                    printTree(condiciones, "");
                    nuevoToken();
                } else if (token == 222 || token == 223) {
                    agregarTablaSimbolos(token);
                    condiciones.addChild(new TreeNode(lexema, lexema));

                    mismoTipo(condiciones);

                    printTree(condiciones, "");
                    nuevoToken();
                } else {
                    getError("numLetra");
                }
            } else {
                getError("relacional");
            }
        } else {
            getError("numLetra");
        }
    }

    private void mismoTipo(TreeNode nodo) {
        if  (nodo != null) {

            String prmLexema = nodo.children.get(0).lexema;
            String operador = nodo.children.get(1).lexema;
            String segLexema = nodo.children.get(2).lexema;

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
        }
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }
}
