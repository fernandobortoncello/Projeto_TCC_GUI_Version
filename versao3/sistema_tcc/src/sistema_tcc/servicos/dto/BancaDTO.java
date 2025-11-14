package sistema_tcc.servicos.dto;

import java.util.List;

/**
 * DTO para exibir os detalhes da banca ao aluno.
 */
public class BancaDTO {
    private final String dataApresentacao;
    private final List<ProfessorDTO> membros;

    public BancaDTO(String dataApresentacao, List<ProfessorDTO> membros) {
        this.dataApresentacao = dataApresentacao;
        this.membros = membros;
    }

    public String getDataApresentacao() {
        return dataApresentacao;
    }

    public List<ProfessorDTO> getMembros() {
        return membros;
    }
}