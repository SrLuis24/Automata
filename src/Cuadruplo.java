public class Cuadruplo {

    public String operador;
    public String operando1;
    public String operando2;
    public String resultado;
    private String valor;

    public Cuadruplo(String operador, String operando1, String operando2, String resultado) {
        this.operador = operador;
        this.operando1 = operando1;
        this.operando2 = operando2;
        this.resultado = resultado;
    }

    public Cuadruplo(String operador, String operando1, String operando2, String resultado, String valor) {
        this.operador = operador;
        this.operando1 = operando1;
        this.operando2 = operando2;
        this.resultado = resultado;
        this.valor = valor;
    }

    public String toString() {
        return String.format("%-10s %-10s %-10s %-10s %-10s", "| " + operador, "| " + operando1, "| " + operando2, "| " + resultado, "|");
    }


}
