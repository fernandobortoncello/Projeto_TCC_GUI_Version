package sistema_tcc.dominio;

/**
 * Representa o ator Aluno.
 */
public class Aluno extends Usuario {

    public Aluno(String matricula, String nome, String senha) {
        super(matricula, nome, senha, Papel.ALUNO);
    }

    public String getMatricula() {
        return getId();
    }
}