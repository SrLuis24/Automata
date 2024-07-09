public class Cuadruplo {

    private String operador;
    private String operando1;
    private String operando2;
    private String resultado;

    public Cuadruplo(String operador, String operando1, String operando2, String resultado) {
        this.operador = operador;
        this.operando1 = operando1;
        this.operando2 = operando2;
        this.resultado = resultado;
    }

    public String toString() {
        return "(" + operador + ", " + operando1 + ", " + operando2 + ", " + resultado + ")";
    }


}
