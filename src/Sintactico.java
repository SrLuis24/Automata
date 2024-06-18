
public class Sintactico {

    private String lexema;
    private int token;
    private Nodo nodo;
    private static int renglon;
    private int fin = 0;

    public Sintactico(Nodo nodo) {
        this.nodo = nodo;
        lexema = nodo.lexema;
        token = nodo.token;
        renglon = nodo.renglon;
        program();
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
                    System.out.println("Se esperaba '.' al final del programa");
                }
                if (nodo == null && token == 116) {
                    if (fin != 0) System.out.println("Se esperaba finalzar un end");
                    else {
                        System.out.println("Perfecto");
                        System.out.println(lexema);
                        return;
                    }
                }
                if (nodo != null) {
                    nuevoToken();
                    if (token == 116) {

                        if (fin != 0) System.out.println("Se esperaba un end");
                        else {
                            System.out.println("Perfecto");
                            renglon = nodo.renglon;
                            return;
                        }
                    } else {
                        System.out.println("Se esperaba '.' al final del programa");
                    }
                }
            } else {
                System.out.println("Se esperaba cierre de línea (Renglón " + renglon + ")");
                System.exit(0);
            }
        } else {
            System.out.println("Se esperaba iniciar con 'program' (Renglón " + renglon + ")");
            System.exit(0);
        }
    }

    private void identificador() {
        nuevoToken();
        if (token == 100) {
            nuevoToken();
            if (token == 117) {
                listaIdentificador();
            }
        } else {
            System.out.println("Se esperaba un identificador (Renglón " + renglon + ")");
            System.exit(0);
        }
    }

    private void identificadorDef() {
        if (token == 100) {
            nuevoToken();
            if (token == 117) {
                listaIdentificador();
            }
        } else {
            System.out.println("Se esperaba un identificador (Renglón " + renglon + ")");
            System.exit(0);
        }
    }

    private void listaIdentificador() {
        nuevoToken();

        if (token == 100) {
            nuevoToken();

            if (token == 117) {
                listaIdentificador();
            }
        } else {
            System.out.println("Se esperaba un identificador (Renglón " + renglon + ")");
            System.exit(0);
        }
    }

    private void bloque() {
        nuevoToken();
        if (nodo != null) {
            if (token == 100 && lexema.equals("var")) {
                definiciones();
            }

            if (token == 205) {
                bloqueEnunciados();
            } else {
                System.out.println("Se esperaba un begin (Renglón " + renglon + ")");
                System.exit(0); // Original
            }
        }
    }

    private void definiciones() {
        listaIdentificador();
        if (token == 119) {
            nuevoToken();

            if (token == 202) {
                nuevoToken();

                if (token == 118) {
                    nuevoToken();
                } else {
                    System.out.println("Se esperaba cierre de la línea (Renglón " + renglon + ")");
                }
            } else {
                System.out.println("Se esperaba tipo Integer (Renglón " + renglon + ")");
            }
        } else {
            System.out.println("Se esperaba ':' (Renglón " + renglon + ")");
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
            nuevoToken();
            condicion();

            if (token == 213) {
                bloque();
            } else {
                System.out.println("Se esperaba 'do' (Renglón " + renglon + ")");
                System.exit(0);
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
                            System.out.println("Se esperaba cierre de línea (Renglón " + renglon + ")");
                            System.exit(0);
                        }
                    } else {
                        System.out.println("Se esperaba un número o letra (Renglón " + renglon + ")");
                        System.exit(0);
                    }
                } else {
                    System.out.println("Se esperaba un operador aritmético (Renglón " + renglon + ")");
                    System.exit(0);
                }
            } else {
                System.out.println("Se esperaba un número o letra (Renglón " + renglon + ")");
                System.exit(0);
            }
        } else if (token == 124) {
            nuevoToken();
            if (token == 100 || token == 101 || token == 102) {
                nuevoToken();

                if (token == 118) {
                    nuevoToken();
                } else if (token == 103 || token == 104 || token == 105 || token == 106 || token == 107) {
                    nuevoToken();
                    if (token == 118){
                        System.out.println("Se esperaba un número o letra (Renglón " + renglon + ")");
                        System.exit(0);
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
                                    System.out.println("Se esperaba un número o letra (Renglón " + renglon + ")");
                                    System.exit(0);
                                    break;
                                }
                            } else {
                                if (token == 100 || token == 101 || token == 102) {
                                    System.out.println("Se esperaba un operador aritmético (Renglón " + renglon + ")");
                                    System.exit(0);
                                } else {
                                    System.out.println("Se esperaba cierre de línea (Renglón " + renglon + ")");
                                    System.exit(0);
                                }
                                break;
                            }
                        } else {
                            System.out.println("Se esperaba un número o letra (Renglón " + renglon + ")");
                            System.exit(0);
                            break;
                        }
                    }

                } else {
                    System.out.println("Se esperaba cierre de línea (Renglón " + renglon + ")");
                    System.exit(0);
                }
            } else {
                System.out.println("Se esperaba un número o letra (Renglón " + renglon + ")");
                System.exit(0);
            }
        }
        else {
            System.out.println("Se esperaba un operador de asignación (Renglón " + renglon + ")" );
            System.exit(0);
        }
    }

    private void alternativas() {
        nuevoToken();
        condicion();

        if (token == 210) {
            nuevoToken();
            bloqueEnunciados();
        } else {
            System.out.println("Se esperaba 'then' (Renglón " + renglon + ")");
            System.exit(0);
        }
        nuevoToken();
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
            System.out.println("Se esperaba un 'begin' (Renglón " + renglon + ")");
            System.exit(0); // Original
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
                    System.out.println("Se esperaba un número o letra (Renglón " + renglon + ")");
                    System.exit(0);
                }
            } else {
                System.out.println("Se esperaba un operador relacional (Renglón " + renglon + ")");
                System.exit(0);
            }
        } else {
            System.out.println("Se esperaba un número o letra (Renglón " + renglon + ")");
            System.exit(0);
        }

    }
}
