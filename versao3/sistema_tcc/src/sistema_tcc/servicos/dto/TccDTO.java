package sistema_tcc.servicos.dto;

import java.util.List;

/**
 * DTO completo para a tela do Aluno.
 * Agrega todos os outros DTOs necessários para a visualização.
 */
public class TccDTO {
    private final String status;
    private final String orientadorNome;
    private final List<OrientacaoDTO> orientacoes;
    private final BancaDTO banca;
    private final AvaliacaoDTO avaliacaoFinal;

    // Construtor complexo
    public TccDTO(String status, String orientadorNome, List<OrientacaoDTO> orientacoes, BancaDTO banca, AvaliacaoDTO avaliacaoFinal) {
        this.status = status;
        this.orientadorNome = orientadorNome;
        this.orientacoes = orientacoes;
        this.banca = banca;
        this.avaliacaoFinal = avaliacaoFinal;
    }

    // Getters para a UI
    public String getStatus() { return status; }
    public String getOrientadorNome() { return orientadorNome; }
    public List<OrientacaoDTO> getOrientacoes() { return orientacoes; }
    public BancaDTO getBanca() { return banca; }
    public AvaliacaoDTO getAvaliacaoFinal() { return avaliacaoFinal; }
}