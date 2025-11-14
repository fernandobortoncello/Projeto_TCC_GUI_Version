package sistema_tcc.dominio;

/**
 * Representa o ator Aluno.
 * Herda os getters seguros de Usuario.
 */
public class Aluno extends Usuario {

    public Aluno(String matricula, String nome, String senha) {
        super(matricula, nome, senha, Papel.ALUNO);
    }

    public String getMatricula() {
        return getId();
    }
}