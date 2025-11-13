package sistema_tcc.repositorio;

import sistema_tcc.dominio.Professor;

/**
 * Interface (Contrato) para persistência de Professores.
 * O AuthServico chama os métodos definidos AQUI.
 */
public interface ProfessorRepositorio {
    void adicionar(Professor p);

    /**
     * Busca um professor pelo seu ID (CPF).
     * Retorna null se não encontrar (tratado pelo AuthServico).
     */
    Professor buscarPorId(String id);
}