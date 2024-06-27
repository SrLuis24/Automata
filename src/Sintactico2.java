
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
            identificador();
            if (token == 118) {
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
            symbolTable.addEntry(lexema, tipoDato, 0, renglon);
            nuevoToken();
            if (token == 117) {
                listaIdentificador();
            }
        } else {
            getError("identificador");
        }
    }

    private void listaIdentificador() {
        nuevoToken();
        if (token == 100) {
            symbolTable.addEntry(lexema, tipoDato, 0, renglon);
            nuevoToken();
            if (token == 117) {
                listaIdentificador();
            }
        } else {
            getError("identificador");
        }
    }

    private void bloque() {
        nuevoToken();
        if (nodo != null) {
            if (token == 221) {
                definiciones();
            }

            if (token == 205) {
                bloqueEnunciados();
            } else {
                getError("begin");
            }
        }
    }

    private void definiciones() {
        listaIdentificador();
        if (token == 119) {
            nuevoToken();
            if (token == 202) {
                tipoDato = "Integer";

                nuevoToken();
                if (token == 118) {
                    nuevoToken();
                } else {
                    getError("cierre");
                }
            } else if (token == 203) {
                tipoDato = "Real";
                nuevoToken();

                if (token == 118) {
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
                nuevoToken();
                operaciones();
            }
        }

        if (token == 206) {
            fin--;
            return;
        }

        if (token == 209) {
            alternativas();
            if (token == 211) {
                alternativasElse();
            }
        }

        if (token == 212) {
            tipoDato = "Palabra reservada";
            symbolTable.addEntry(lexema, tipoDato, 0, renglon);

            nuevoToken();
            condicion();
            if (token == 213) {
                bloque();
            } else {
                getError("do");
            }
        }

        listaEnunciados();
    }

    private void operaciones() {
        if (token == 108 || token == 109 || token == 110 || token == 111 || token == 112 || token == 113) {
            nuevoToken();
            if (token == 100 || token == 101 || token == 102) {
                nuevoToken();
                if (token == 103 || token == 104 || token == 105 || token == 106 || token == 107) {
                    nuevoToken();
                    if (token == 100 || token == 101 || token == 102) {
                        nuevoToken();
                        if (token == 118) {
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
            System.out.println(lexema + " " + token);
            nuevoToken();
            if (token == 100 || token == 101 || token == 102) {
                nuevoToken();
                if (token == 118) {
                    nuevoToken();
                } else if (token == 103 || token == 104 || token == 105 || token == 106 || token == 107) {
                    nuevoToken();
                    if (token == 118) {
                        getError("numLetra");
                    }
                    while (token != 118) {
                        if (token == 100 || token == 101 || token == 102) {
                            nuevoToken();
                            if (token == 118) {
                                break;
                            }
                            if (token == 103 || token == 104 || token == 105 || token == 106 || token == 107) {
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
            fin++;
            listaEnunciados();
        } else {
            getError("begin");
        }
    }

    private void condicion() {
        if (token == 100 || token == 101 || token == 102) {
            nuevoToken();
            if (token == 108 || token == 109 || token == 110 || token == 111 || token == 112 || token == 113) {
                nuevoToken();
                if (token == 100 || token == 101 || token == 102) {
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
