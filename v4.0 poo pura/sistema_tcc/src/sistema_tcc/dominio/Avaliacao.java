package sistema_tcc.dominio;
public class Avaliacao {
    private final double nota;
    private final String parecer;
    public Avaliacao(double n, String p) { this.nota = n; this.parecer = p; }
    public double lerNota() { return nota; }
    public String lerParecer() { return parecer; }
    public String lerVeredito() { return nota >= 6 ? "APROVADO" : "REPROVADO"; }
}