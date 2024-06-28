
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
                System.out.println("Se esperaba cierre de línea (Renglón " + renglon + ")");
                break;
            default:
                System.out.println("Hay un error.");
        }

        /*
        errores.put("program", "Se esperaba iniciar con 'program' (Renglón " + renglon + ")");
        errores.put("end", "Se esperaba finalizar un end");
        errores.put("identificador", "Se esperaba un identificador (Renglón " + renglon + ")");
        errores.put("begin", "Se esperaba un begin (Renglón " + renglon + ")");
        errores.put("tipoDato", "Se esperaba tipo de dato (Renglón " + renglon + ")");
        errores.put(":", "Se esperaba ':' (Renglón " + renglon + ")");
        errores.put("do", "Se esperaba 'do' (Renglón " + renglon + ")");
        errores.put("numLetra", "Se esperaba un número o letra (Renglón " + renglon + ")");
        errores.put("aritmetico", "Se esperaba un operador aritmético (Renglón " + renglon + ")");
        errores.put("asignacion", "Se esperaba un operador de asignación (Renglón " + renglon + ")");
        errores.put("then", "Se esperaba 'then' (Renglón " + renglon + ")");
        errores.put("relacional", "Se esperaba un operador relacional (Renglón " + renglon + ")");
        */

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
            agregarTablaSimbolos(token);
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
            System.out.println(indent + node.value);
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
                tipoDato = "Integer";
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
            } else {
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

        listaEnunciados();
    }

    private void agregarTablaSimbolos(int token) {

        if (token == 100) {
            tipoDato = "String";
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
        }
        else if (token >= 200) {
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
        if (token == 100 || token == 101 || token == 102) {
            agregarTablaSimbolos(token);
            nuevoToken();
            if (token == 108 || token == 109 || token == 110 || token == 111 || token == 112 || token == 113) {
                agregarTablaSimbolos(token);
                nuevoToken();
                if (token == 100 || token == 101 || token == 102) {
                    agregarTablaSimbolos(token);
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

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }
}
