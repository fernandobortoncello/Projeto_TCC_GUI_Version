package sistema_tcc.repositorio;

import sistema_tcc.dominio.Professor;
import java.util.List;

/**
 * Interface (Contrato) para o repositório de Professores.
 */
public interface ProfessorRepositorio {
    /**
     * Busca um professor pelo seu ID (CPF).
     * @return O Professor, ou null se não for encontrado.
     */
    Professor buscarPorId(String id);

    /**
     * Retorna todos os professores (para preencher listas da banca, UC4).
     */
    List<Professor> listarTodos();
}