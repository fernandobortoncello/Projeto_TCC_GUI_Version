package sistema_tcc.infra;

import sistema_tcc.dominio.Aluno;
import sistema_tcc.dominio.Professor;
import sistema_tcc.repositorio.UsuarioRepositorio;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Implementação FAKE (em memória) do repositório de Usuários.
 * Simula o banco de dados.
 */
public class MockUsuarioRepositorio implements UsuarioRepositorio {
    private final Map<String, Aluno> dbAlunos = new HashMap<>();
    private final Map<String, Professor> dbProfessores = new HashMap<>();

    @Override
    public void salvarAluno(Aluno aluno) {
        dbAlunos.put(aluno.getId(), aluno);
    }

    @Override
    public void salvarProfessor(Professor professor) {
        dbProfessores.put(professor.getCpf(), professor);
    }

    @Override
    public Optional<Aluno> buscarAlunoPorCpf(String cpf) {
        return Optional.ofNullable(dbAlunos.get(cpf));
    }

    @Override
    public Optional<Professor> buscarProfessorPorCpf(String cpf) {
        return Optional.ofNullable(dbProfessores.get(cpf));
    }
}