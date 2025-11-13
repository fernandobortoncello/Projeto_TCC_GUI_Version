package sistema_tcc.repositorio;

import sistema_tcc.dominio.Aluno;

/**
 * Interface (Contrato) para persistência de Alunos.
 * O AuthServico chama os métodos definidos AQUI.
 */
public interface AlunoRepositorio {
    void adicionar(Aluno a);

    /**
     * Busca um aluno pela sua matrícula.
     * Retorna null se não encontrar (tratado pelo AuthServico).
     */
    Aluno buscarPorMatricula(String matricula);
}