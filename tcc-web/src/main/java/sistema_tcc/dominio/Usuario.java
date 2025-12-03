package sistema_tcc.dominio;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_usuario")
public abstract class Usuario {

    @Id
    protected String id;
    protected String nome;
    protected String senha;

    protected Usuario() {}

    public Usuario(String id, String nome, String senha) {
        this.id = id;
        this.nome = nome;
        this.senha = senha;
    }

    public boolean autenticar(String tentativa) {
        return this.senha.equals(tentativa);
    }

    public String getId() { return id; }
    public String getNome() { return nome; }
}