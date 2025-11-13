package sistema_tcc.dominio;

/**
 * Representa a avaliação final do TCC (UC5).
 */
public class Avaliacao {
    private final double notaFinal;
    private final String anotacoes;
    private final String status; // APROVADO ou REPROVADO

    public Avaliacao(double notaFinal, String anotacoes) {
        if (notaFinal < 0 || notaFinal > 10) {
            throw new IllegalArgumentException("Nota deve ser entre 0 e 10.");
        }
        this.notaFinal = notaFinal;
        this.anotacoes = (anotacoes == null) ? "" : anotacoes;

        // Regra de Negócio da ATA (RF020)
        this.status = (notaFinal >= 6) ? "APROVADO" : "REPROVADO";
    }

    // Getters
    public double getNotaFinal() { return notaFinal; }
    public String getAnotacoes() { return anotacoes; }
    public String getStatus() { return status; }

    @Override
    public String toString() {
        return "Nota: " + notaFinal + " (" + status + ")";
    }
}