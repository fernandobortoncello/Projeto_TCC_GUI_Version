package sistema_tcc.servicos.dto;

/**
 * DTO para exibir o histórico de orientações ao aluno.
 */
public class OrientacaoDTO {
    private final String data;
    private final String descricao;

    public OrientacaoDTO(String data, String descricao) {
        this.data = data;
        this.descricao = descricao;
    }

    public String getData() {
        return data;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return data + " - " + descricao;
    }
}