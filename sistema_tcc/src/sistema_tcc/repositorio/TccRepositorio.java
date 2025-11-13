package sistema_tcc.repositorio;

import sistema_tcc.dominio.TCC;
import sistema_tcc.dominio.TccStatus; // Import necessário
import java.util.List;

/**
 * Interface (Contrato) para o repositório de TCCs.
 * Define quais métodos de acesso a dados SÃO permitidos.
 *
 * CORREÇÃO: Adicionado o método 'buscarPorStatus' que faltava.
 */
public interface TccRepositorio {

    /**
     * Salva (cria ou atualiza) um TCC no banco de dados.
     */
    TCC salvar(TCC tcc);

    /**
     * Busca um TCC pelo seu ID único.
     */
    TCC buscarPorId(String id);

    /**
     * Retorna todos os TCCs cadastrados.
     */
    List<TCC> listarTodos();

    /**
     * MÉTODO ADICIONADO (RF004): Retorna TCCs que correspondem a um status.
     */
    List<TCC> buscarPorStatus(TccStatus status);
}