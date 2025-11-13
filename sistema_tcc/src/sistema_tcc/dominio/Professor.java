package sistema_tcc.dominio;

/**
 * Entidade Professor (Especialização de Usuário).
 */
public class Professor extends Usuario {
    // private List<AreaConhecimento> areasInteresse; // Exemplo de expansão futura

    public Professor(String nome, String cpf, String senha) {
        // Pode ser PROFESSOR ou COORDENADOR (RF012)
        super(nome, cpf, senha, Papel.PROFESSOR);
    }
}