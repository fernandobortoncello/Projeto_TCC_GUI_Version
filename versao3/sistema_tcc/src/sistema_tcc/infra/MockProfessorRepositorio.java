package sistema_tcc.infra;

import sistema_tcc.dominio.Papel;
import sistema_tcc.dominio.Professor;
import sistema_tcc.repositorio.ProfessorRepositorio;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simulação de um banco de dados de Professores em memória.
 */
public class MockProfessorRepositorio implements ProfessorRepositorio {

    private final Map<String, Professor> db = new HashMap<>();

    public void popularDadosDemo() {
        Professor p1 = new Professor("111222", "Willian Bolzan", "prof123", Papel.PROFESSOR);
        Professor p2 = new Professor("333444", "Thiago Martins Oliveira", "prof123", Papel.PROFESSOR);
        Professor p3 = new Professor("555666", "Tiago José Griebeler Carvalho", "prof123", Papel.COORDENADOR);

        db.put(p1.getId(), p1);
        db.put(p2.getId(), p2);
        db.put(p3.getId(), p3);
        System.out.println("LOG DB: " + db.size() + " professores carregados.");
    }

    @Override
    public Professor buscarPorId(String id) {
        return db.get(id);
    }

    @Override
    public List<Professor> listarTodos() {
        return new ArrayList<>(db.values());
    }
}