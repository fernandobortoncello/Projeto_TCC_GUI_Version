package sistema_tcc.dominio;

/**
 * Classe base abstrata para Aluno e Professor (RF001).
 * Totalmente encapsulada.
 */
public abstract class Usuario {

    private String id; // Pode ser CPF (Professor) ou Matrícula (Aluno)
    private String nome;
    private String senhaHasheada; // RNF-SEG01
    private Papel papel;

    public Usuario(String id, String nome, String senha, Papel papel) {
        this.id = id;
        this.nome = nome;
        this.senhaHasheada = senha; // Simulação de hash
        this.papel = papel;
    }

    // --- Getters de Leitura Segura ---
    // A UI precisa destes dados para exibição.

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Papel getPapel() {
        return papel;
    }

    // --- Métodos de Comportamento (Encapsulado) ---

    /**
     * Lógica de verificação de senha (Information Expert).
     * Não expõe a senha hasheada.
     */
    public boolean verificarSenha(String senha) {
        // Em um app real: return BCrypt.checkpw(senha, this.senhaHasheada);
        return this.senhaHasheada.equals(senha);
    }

    @Override
    public String toString() {
        return nome; // Usado pelo JavaFX em ComboBoxes
    }
}