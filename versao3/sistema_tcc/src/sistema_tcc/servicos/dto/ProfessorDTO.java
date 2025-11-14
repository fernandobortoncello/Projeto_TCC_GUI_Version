package sistema_tcc.servicos.dto;

/**
 * DTO para listar professores (ex: na seleção da banca).
 * Expõe apenas ID e Nome.
 */
public class ProfessorDTO {
    private final String id;
    private final String nome;

    public ProfessorDTO(String id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return nome;
    }
}