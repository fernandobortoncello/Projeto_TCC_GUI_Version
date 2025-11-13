package sistema_tcc.infra;

import sistema_tcc.dominio.Aluno;
import sistema_tcc.repositorio.AlunoRepositorio;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementação FAKE (em memória) do repositório de Alunos.
 */
public class MockAlunoRepositorio implements AlunoRepositorio {

    // Simula a tabela "alunos"
    private final Map<String, Aluno> dbAlunos = new HashMap<>();

    /**
     * ADICIONADO: Método para popular o mock com dados de exemplo.
     */
    public void popularDadosDemo() {
        // Dados baseados no seu arquivo SistemaTCC.java original
        Aluno aluno1 = new Aluno("Aline Espindola", "2025001", "senha123");
        Aluno aluno2 = new Aluno("Fernando Bortoncello", "2025002", "senha123");

        this.adicionar(aluno1);
        this.adicionar(aluno2);
        System.out.println("LOG DB: " + dbAlunos.size() + " alunos carregados.");
    }

    @Override
    public void adicionar(Aluno a) {
        // Usa a matrícula (CPF) como chave
        dbAlunos.put(a.getMatricula(), a);
    }

    @Override
    public Aluno buscarPorMatricula(String matricula) {
        // Retorna o aluno ou null (tratado pelo AuthServico)
        return dbAlunos.get(matricula);
    }
}