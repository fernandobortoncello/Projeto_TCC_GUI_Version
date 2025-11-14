package sistema_tcc.repositorio;

import sistema_tcc.dominio.Aluno;

/**
 * Interface (Contrato) para o repositório de Alunos.
 */
public interface AlunoRepositorio {
    /**
     * Busca um aluno pela sua matrícula.
     * @return O Aluno, ou null se não for encontrado.
     */
    Aluno buscarPorMatricula(String matricula);
}