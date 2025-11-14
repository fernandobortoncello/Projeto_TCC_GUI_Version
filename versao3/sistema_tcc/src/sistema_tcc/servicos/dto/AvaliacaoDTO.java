package sistema_tcc.servicos.dto;

/**
 * DTO para exibir a avaliação final ao aluno.
 */
public class AvaliacaoDTO {
    private final double notaFinal;
    private final String anotacoes;
    private final String status; // APROVADO ou REPROVADO

    public AvaliacaoDTO(double notaFinal, String anotacoes, String status) {
        this.notaFinal = notaFinal;
        this.anotacoes = anotacoes;
        this.status = status;
    }

    public double getNotaFinal() {
        return notaFinal;
    }

    public String getAnotacoes() {
        return anotacoes;
    }

    public String getStatus() {
        return status;
    }
}