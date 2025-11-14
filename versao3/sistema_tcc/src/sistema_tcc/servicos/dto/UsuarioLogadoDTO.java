package sistema_tcc.servicos.dto;

import sistema_tcc.dominio.Papel;

/**
 * DTO (Data Transfer Object) - Camada Intermediária.
 * Contém APENAS os dados do usuário que a UI precisa saber.
 * Não expõe senha ou lógica de negócio.
 */
public class UsuarioLogadoDTO {
    private final String id;
    private final String nome;
    private final Papel papel;

    public UsuarioLogadoDTO(String id, String nome, Papel papel) {
        this.id = id;
        this.nome = nome;
        this.papel = papel;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Papel getPapel() {
        return papel;
    }
}