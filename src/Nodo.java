public class Nodo {
    String lexema; // Lexema asignado al nodo
    int token; // Token o id del nodo
    int renglon;
    Nodo vaciar = null; // Nodo hijo que será null al crearse el primer nodo

    Nodo(String lexema, int token, int renglon) {
        this.lexema = lexema;
        this.token = token;
        this.renglon = renglon;
    };
}
