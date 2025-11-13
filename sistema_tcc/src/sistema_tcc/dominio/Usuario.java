package sistema_tcc.dominio;

public abstract class Usuario {
    protected String nome;
    protected String cpf; // Usado como ID universal
    protected String senha; // Em um app real, seria um hash (RNF-SEG01)
    protected Papel papel;

    public Usuario(String nome, String cpf, String senha, Papel papel) {
        this.nome = nome;
        this.cpf = cpf;
        this.senha = senha;
        this.papel = papel;
    }

    public boolean verificarSenha(String tentativa) {
        return this.senha.equals(tentativa);
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public Papel getPapel() {
        return papel;
    }
}