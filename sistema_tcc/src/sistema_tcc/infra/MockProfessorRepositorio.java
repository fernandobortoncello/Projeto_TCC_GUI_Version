package sistema_tcc.infra;

import sistema_tcc.dominio.Professor;
import sistema_tcc.repositorio.ProfessorRepositorio;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementação FAKE (em memória) do repositório de Professores.
 */
public class MockProfessorRepositorio implements ProfessorRepositorio {

    // Simula a tabela "professores"
    private final Map<String, Professor> dbProfessores = new HashMap<>();

    /**
     * ADICIONADO: Método para popular o mock com dados de exemplo.
     */
    public void popularDadosDemo() {
        // Dados baseados no seu arquivo SistemaTCC.java original
        Professor prof1 = new Professor("Willian Bolzan", "111222", "prof123");
        Professor prof2 = new Professor("Professor Convidado", "333444", "prof123");
        Professor prof3 = new Professor("Professor Avaliador", "555666", "prof123");

        this.adicionar(prof1);
        this.adicionar(prof2);
        this.adicionar(prof3);
        System.out.println("LOG DB: " + dbProfessores.size() + " professores carregados.");
    }

    @Override
    public void adicionar(Professor p) {
        dbProfessores.put(p.getCpf(), p);
    }

    @Override
    public Professor buscarPorId(String id) {
        // Retorna o professor ou null (tratado pelo AuthServico)
        return dbProfessores.get(id);
    }
}