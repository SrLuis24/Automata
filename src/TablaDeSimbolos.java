public class TablaDeSimbolos {

    private String tipoDato;
    private String token;
    private String valor = null;
    private int renglon;

    public TablaDeSimbolos(String token, String tipoDato, int renglon) {
        this.tipoDato = tipoDato;
        this.token = token;
        this.renglon = renglon;
    }

    public String getToken() {
        return this.token;
    }

    public String getTipoDato() {
        return this.tipoDato;
    }

    public String getValor() {
        return this.valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public int getRenglon() {
        return this.renglon;
    }

    public void setRenglon(int renglon) {
        this.renglon = renglon;
    }
}
