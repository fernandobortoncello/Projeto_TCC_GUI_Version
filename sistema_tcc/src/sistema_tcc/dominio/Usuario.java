package sistema_tcc.dominio;

/**
 * Classe base abstrata para Aluno e Professor (RF001).
 * Segue o padrão Information Expert para encapsular a lógica de senha.
 */
public abstract class Usuario {

    private String id; // Pode ser CPF (Professor) ou Matrícula (Aluno)
    private String nome;
    private String senhaHasheada; // RNF-SEG01
    private Papel papel;

    public Usuario(String id, String nome, String senha, Papel papel) {
        this.id = id;
        this.nome = nome;
        // Em um app real, aqui entraria o BCrypt.hashpw(senha, BCrypt.gensalt())
        this.senhaHasheada = senha; // Simulação
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

    /**
     * Lógica de verificação de senha (Information Expert).
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