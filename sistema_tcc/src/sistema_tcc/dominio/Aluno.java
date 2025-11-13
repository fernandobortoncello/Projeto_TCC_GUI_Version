package sistema_tcc.dominio;

/**
 * Entidade Aluno (Especialização de Usuário).
 * O CPF será usado como matrícula para simplificar.
 */
public class Aluno extends Usuario {
    public Aluno(String nome, String cpf, String senha) {
        super(nome, cpf, senha, Papel.ALUNO);
    }

    // Matrícula é o CPF neste modelo
    public String getMatricula() {
        return super.getCpf();
    }
}