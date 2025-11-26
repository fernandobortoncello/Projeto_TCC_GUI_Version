package sistema_tcc.dominio;

import sistema_tcc.dominio.tipo.Papel;

public abstract class Usuario {
    protected String id;
    protected String nome;
    protected String senha;
    protected Papel papel;

    public Usuario(String id, String nome, String senha, Papel papel) {
        this.id = id; this.nome = nome; this.senha = senha; this.papel = papel;
    }

    // Comportamentos essenciais
    public boolean autenticar(String tentativa) { return this.senha.equals(tentativa); }
    public boolean identificar(String idBusca) { return this.id.equals(idBusca); }

    // Leitura básica (Read-Only)
    public String lerId() { return id; }
    public String lerNome() { return nome; }
}