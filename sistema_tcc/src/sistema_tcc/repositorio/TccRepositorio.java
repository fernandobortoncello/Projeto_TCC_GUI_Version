package sistema_tcc.repositorio;

import sistema_tcc.dominio.Aluno;
import sistema_tcc.dominio.Professor;
import sistema_tcc.dominio.TCC;
import sistema_tcc.dominio.TccStatus;
import java.util.List;

/**
 * Interface (Contrato) para o repositório de TCCs.
 */
public interface TccRepositorio {

    TCC salvar(TCC tcc);

    TCC buscarPorId(String id);

    List<TCC> listarTodos();

    /**
     * Retorna TCCs que correspondem a um status (RF004).
     */
    List<TCC> buscarPorStatus(TccStatus status);

    /**
     * Retorna TCCs orientados por um professor específico (UC3).
     */
    List<TCC> buscarPorOrientador(Professor professor);

    /**
     * Retorna o TCC de um aluno específico (Tela do Aluno).
     */
    TCC buscarPorAluno(Aluno aluno);

    /**
     * Retorna TCCs prontos para banca (UC4).
     */
    List<TCC> buscarTccsParaDefinirBanca(Professor professor);

    /**
     * Retorna TCCs prontos para finalização (UC5).
     */
    List<TCC> buscarTccsParaFinalizar(Professor professor);
}