package sistema_tcc.infra;

import sistema_tcc.dominio.Aluno;
import sistema_tcc.repositorio.AlunoRepositorio;
import java.util.HashMap;
import java.util.Map;

/**
 * Simulação de um banco de dados de Alunos em memória.
 */
public class MockAlunoRepositorio implements AlunoRepositorio {

    private final Map<String, Aluno> db = new HashMap<>();

    public void popularDadosDemo() {
        Aluno a1 = new Aluno("2025001", "Aline de Abreu Espindola", "senha123");
        Aluno a2 = new Aluno("2025002", "Fernando Bortoncello", "senha123");

        db.put(a1.getMatricula(), a1);
        db.put(a2.getMatricula(), a2);
        System.out.println("LOG DB: " + db.size() + " alunos carregados.");
    }

    @Override
    public Aluno buscarPorMatricula(String matricula) {
        return db.get(matricula);
    }
}