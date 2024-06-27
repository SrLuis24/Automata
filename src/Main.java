import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class Main {
    static Nodo actual = null;
    static Nodo nodo;
    static int estado = 0;
    static int columna;
    static int token;
    static int simbolo = 0;
    static String lexema = "";
    static int renglon = 1;
    static Transiciones transiciones = new Transiciones();
    static int[][] matriz = transiciones.getMatriz();

    static String[][] palabrasReservadas = transiciones.getPalabrasReservadas();

    static String[][] errores = transiciones.getErrores();

    static RandomAccessFile rfa;
    public static boolean errorEcontrado = false;

    public static void main(String[] args) {
        try {
            String directorio = "archivoSintactico.txt"; // El nombre del archivo del léxico
            File dirFile = new File(directorio); // Obtiene el archivo que leera del léxico

            if (!dirFile.exists()) {
                /* Se encarga de crear el archivo en la
                    dirección del proyecto en caso de no existir */
                dirFile.createNewFile();
                System.out.println("Archivo creado.");
            }

            rfa = new RandomAccessFile(dirFile, "r");

            /* El bucle recorrerá todos los caracteres
                que se encuentren en el archivo */
            while (simbolo != -1){
                simbolo = rfa.read(); // Obtiene el caracter que se leyó pero en byte

                if (Character.isLetter(((char) simbolo))){
                    // Si el caracter que se leyó cuenta como una letra irá a la columna de ID
                    columna = 0;
                } else if (Character.isDigit((char) simbolo)){
                    // Si el caracter que se leyó cuenta como una letra irá a la columna de Dígito
                    columna = 2;
                } else{
                    // El switch se encarga de verificar el tipo de
                    // caracter y seleccionar la columna de dicho caracter
                    switch ((char) simbolo) {
                        case '_' : columna = 1;
                            break;
                        case '+' : columna = 3;
                            break;
                        case '-' : columna = 4;
                            break;
                        case '*' : columna = 5;
                            break;
                        case '/' : columna = 6;
                            break;
                        case '>' : columna = 7;
                            break;
                        case '<' : columna = 8;
                            break;
                        case '=' : columna = 9;
                            break;
                        case '!' : columna = 10;
                            break;
                        case '(' : columna = 11;
                            break;
                        case ')' : columna = 12;
                            break;
                        case '.' : columna = 13;
                            break;
                        case ',' : columna = 14;
                            break;
                        case ';' : columna = 15;
                            break;
                        case '"' : columna = 16;
                            break;
                        case ' ' : columna = 17;
                            break;
                        case ':': columna = 21;
                            break;
                        case 9 : columna = 18;
                            break;
                        case 10 :
                            // En caso de que se termine la linea manda a dicha columna
                            // y se suma 1 al renglón para saber en que renglón se encuentra
                            // en caso de falló
                            columna = 20;
                            renglon++;
                            break;
                        default :
                            // Si no encuentra el caracter se da como resultado el fin de archivo
                            columna=19;
                    }
                }

                token = matriz[estado][columna]; // Obtiene el valor de ID o un nuevo estado dependiendo donde se ubique

                // Verifica si el token que se obtuvo de la matriz sea menor de 100
                // y en caso de serlo significa que es un estado lo que se obtuvo
                if (token < 100){
                    estado = token; // Se le asigna el valor que se obtuvo a estado que será el nuevo valor
                    if (estado == 0 ){
                        lexema = "";
                    }else {
                        // Se tomará como una palabra o un conjunto de caracteres que se agregara al lexema
                        // Esto formará una cadena
                        lexema += (char) simbolo;
                    }

                }else if(token < 500){
                    if (token == 100) {
                        // Al ser id de 100 significa que puede ser una palabra
                        // lo que se tendrá que revisar que no sea una palabra reservada
                        isPalabraReservada();
                    }

                    // Se verifica que no sea un token con el cual se puede formar otro caracter
                    if (tokenEsperado(token)){
                        rfa.seek(rfa.getFilePointer()-1);

                    }else{
                        // Se tomará como una palabra o un conjunto de caracteres que se agregara al lexema
                        // Esto formará una cadena
                        lexema += (char) simbolo;

                    }

                    // Se agregará el caracter o el conjunto de caracteres como un nodo
                    agregarNodo();
                    estado = 0; // Se regresa al estado 0 para comenzar una nueva lectura
                    lexema = ""; // Se vacía lexema para no continuar con la cadena ya hecha.

                }else{
                    // Se obtuvo un token de 500 o mayor lo que será un error
                    imprimirErrores();
                    // Se regresa el estado a 0 para comenzar una nueva lectura
                    estado = 0;
                }
            }



            // En caso de ya no encontrar más caractéres se imprimen todos los nodos
            imprimirResultado();



        } catch (IOException ex) {
            // Mostrar el mensaje de error en caso de encontrar alguno
            System.out.println(ex);
        }
    }

    private static void agregarNodo(){
        // Se crea un nuevo nodo asignando el lexema y token
        Nodo n = new Nodo(lexema, token, renglon);

        // Al ser el primer nodo actual es null
        if (actual == null){
            // Se le asigna el nodo a actual
            actual = n;
            // nodo se le asigna a actual para nodo
            nodo = actual;
        }else{
            // Se asigna al nodo hijo el valor del nuevo nodo
            nodo.vaciar = n;
            // nodo toma el valor del nodo creado
            nodo = n;
        }
    }

    private static void isPalabraReservada(){
        // Se recorre el arreglo de palabras reservadas para encontrar una similitud
        for (String[] palabraReservada : palabrasReservadas){
            // Se verifica que la palabra reservada tomada sea igual al lexema
            if (lexema.equals(palabraReservada[0])){
                // Se le asigna al token el valor de la palabra reservada
                token = Integer.valueOf(palabraReservada[1]);
            }
        }
    }

    private static boolean tokenEsperado(int token) {
        // Retorna un valor boolean al encontrar el token almenos uno
        return token == 100 || token == 101 || token == 102 ||
                token == 106 || token == 110|| token >= 200;
    }
    private static void imprimirErrores() {
        // Se verifica que el token tenga un valor mayor o igual a 500 y el caracter sea o no un fin de línea
        if((simbolo != -1 && token >= 500) || (simbolo == -1 && token >= 500)){
            // Se recorre el arreglo de errores para verificar cual es el error correspondiente
            for (String[] errores : errores){
                // Se verifica si el token tiene el mismo valor que el error tomado del arreglo
                if (token == Integer.valueOf(errores[1])){
                    // Se imprime el mensaje de error
                    System.out.println(errores[0] + ", Código de error: " + token + ", renglón del error: " + renglon);
                    errorEcontrado = true;
                    break; // Se termina el bucle debido a que se encontro el error esperado
                }
            }
        }

    }
    private static void imprimirResultado(){
        nodo = actual; // Se le asigna a nodo los nodos actuales

        //new Sintactico(nodo);
        Sintactico2 sintactico = new Sintactico2(nodo);

        SymbolTable symbolTable = sintactico.getSymbolTable();
        System.out.println("Tabla de Simbolos: ");
        System.out.println(symbolTable.toString());

        int renglonFinal = Sintactico2.getRenglon();

        System.out.println(); // Se dará un espacio en blanco para separar de los mensajes de error
        // Se recorre los nodos en caso de ser diferente de null
        while (nodo != null){
            // Se imprime el lexema y el token
            //    System.out.println(nodo.lexema + "  Token: " + nodo.token);
            // Se le asigna al nodo el nodo hijo
            // el primer nodo creado obtiene el valor de null
            // si el nodo hijo siguiente es null se parará el bucle
            nodo = nodo.vaciar;

            if (nodo.token == 116 && nodo.renglon == renglonFinal) {
                //System.out.println(nodo.lexema + "  Token: " + nodo.token);
                break;
            }

        }

    }

    private static void printTree(TreeNode node, String indent) {
        if (node != null) {
            System.out.println(indent + node.value + " :  " + node.lexema);
            for (TreeNode child : node.children) {
                printTree(child, indent + "  ");
            }
        }
    }


}

  