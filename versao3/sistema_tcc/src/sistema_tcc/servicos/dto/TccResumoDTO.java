package sistema_tcc.servicos.dto;

/**
 * DTO para listas (ListView, ComboBox).
 * Contém o mínimo de informação para exibir e selecionar um TCC.
 */
public class TccResumoDTO {
    private final String id;
    private final String tituloEAutor;

    public TccResumoDTO(String id, String tituloEAutor) {
        this.id = id;
        this.tituloEAutor = tituloEAutor;
    }

    public String getId() {
        return id;
    }

    /**
     * O JavaFX usa toString() para exibir o nome no ComboBox/ListView.
     */
    @Override
    public String toString() {
        return tituloEAutor;
    }
}