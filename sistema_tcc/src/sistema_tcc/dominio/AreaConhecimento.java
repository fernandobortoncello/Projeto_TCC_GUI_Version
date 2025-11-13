package sistema_tcc.dominio;

/**
 * Representa as áreas de conhecimento para um TCC (UC1).
 * Usar um Enum garante a consistência dos dados e facilita
 * o uso em ComboBoxes (listas de seleção) no JavaFX.
 */
public enum AreaConhecimento {

    // Lista de constantes do enum
    ENGENHARIA_SOFTWARE("Engenharia de Software"),
    BANCO_DE_DADOS("Banco de Dados"),
    REDES_DE_COMPUTADORES("Redes de Computadores"),
    INTELIGENCIA_ARTIFICIAL("Inteligência Artificial"),
    SEGURANCA_INFORMACAO("Segurança da Informação"),
    DESENVOLVIMENTO_WEB("Desenvolvimento Web");

    // Atributo para o nome amigável
    private final String nomeAmigavel;

    /**
     * Construtor privado do enum.
     * @param nomeAmigavel O nome formatado para exibição na UI.
     */
    AreaConhecimento(String nomeAmigavel) {
        this.nomeAmigavel = nomeAmigavel;
    }

    /**
     * Retorna o nome formatado para exibição.
     */
    public String getNomeAmigavel() {
        return nomeAmigavel;
    }

    /**
     * Sobrescreve o método toString() para que o JavaFX
     * (ComboBox, ListView) exiba o nome amigável automaticamente.
     */
    @Override
    public String toString() {
        return this.nomeAmigavel;
    }
}