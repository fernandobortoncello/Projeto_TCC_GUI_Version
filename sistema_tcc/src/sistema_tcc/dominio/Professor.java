package sistema_tcc.dominio;

/**
 * Representa o ator Professor.
 */
public class Professor extends Usuario {

    public Professor(String cpf, String nome, String senha, Papel papel) {
        super(cpf, nome, senha, papel);
    }

    public String getCpf() {
        return getId();
    }
}