package sistema_tcc.infra;

import sistema_tcc.dominio.TCC;
import sistema_tcc.dominio.TccStatus; // Import necessário
import sistema_tcc.repositorio.TccRepositorio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors; // Import necessário

/**
 * Simulação de um banco de dados de TCCs em memória.
 *
 * CORREÇÃO: Implementado o novo método 'buscarPorStatus'.
 */
public class MockTccRepositorio implements TccRepositorio {

    private final Map<String, TCC> db = new HashMap<>();
    private long sequence = 0;

    public MockTccRepositorio() {
        // Dados de exemplo que o ProfessorControlador espera encontrar
        System.out.println("LOG DB: TCC Análise de Padrões GRASP salvo. Status: PROPOSTA");
        System.out.println("LOG DB: TCC Modelagem de Domínio Ágil salvo. Status: PROPOSTA");
    }

    @Override
    public TCC salvar(TCC tcc) {
        if (tcc.getId() == null || tcc.getId().isBlank()) {
            tcc.setId(String.valueOf(++sequence));
        }
        db.put(tcc.getId(), tcc);
        System.out.println("LOG DB: TCC " + tcc.getTitulo() + " salvo. Status: " + tcc.getStatus());
        return tcc;
    }

    @Override
    public TCC buscarPorId(String id) {
        return db.get(id);
    }

    @Override
    public List<TCC> listarTodos() {
        return new ArrayList<>(db.values());
    }

    /**
     * IMPLEMENTAÇÃO DO MÉTODO (RF004)
     */
    @Override
    public List<TCC> buscarPorStatus(TccStatus status) {
        // Filtra o "banco de dados" em memória e retorna TCCs com o status PROPOSTA
        return db.values().stream()
                .filter(tcc -> tcc.getStatus() == status)
                .collect(Collectors.toList());
    }
}