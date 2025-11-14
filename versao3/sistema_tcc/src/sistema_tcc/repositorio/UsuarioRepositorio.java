package sistema_tcc.repositorio;

import sistema_tcc.dominio.Aluno;
import sistema_tcc.dominio.Professor;
import java.util.Optional;

/**
 * Contrato de persistência para Usuários.
 */
public interface UsuarioRepositorio {
    void salvarAluno(Aluno aluno);
    void salvarProfessor(Professor professor);
    Optional<Aluno> buscarAlunoPorCpf(String cpf);
    Optional<Professor> buscarProfessorPorCpf(String cpf);
    // Em um app real, o método de busca seria unificado
}