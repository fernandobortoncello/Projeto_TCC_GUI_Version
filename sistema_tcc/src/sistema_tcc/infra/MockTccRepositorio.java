package sistema_tcc.infra;

import sistema_tcc.dominio.*;
import sistema_tcc.repositorio.AlunoRepositorio;
import sistema_tcc.repositorio.ProfessorRepositorio;
import sistema_tcc.repositorio.TccRepositorio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Simulação de um banco de dados de TCCs em memória.
 */
public class MockTccRepositorio implements TccRepositorio {

    private final Map<String, TCC> db = new HashMap<>();
    private long sequence = 0;

    /**
     * Popular TCCs de exemplo (agora depende de Alunos e Professores).
     */



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

    @Override
    public List<TCC> buscarPorStatus(TccStatus status) {
        return db.values().stream()
                .filter(tcc -> tcc.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Override
    public List<TCC> buscarPorOrientador(Professor professor) {
        return db.values().stream()
                .filter(tcc -> tcc.getOrientador() != null &&
                        tcc.getOrientador().getId().equals(professor.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public TCC buscarPorAluno(Aluno aluno) {
        return db.values().stream()
                .filter(tcc -> tcc.getAutor().getId().equals(aluno.getId()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<TCC> buscarTccsParaDefinirBanca(Professor professor) {
        return db.values().stream()
                .filter(tcc -> tcc.getOrientador() != null &&
                        tcc.getOrientador().getId().equals(professor.getId()) &&
                        tcc.getStatus() == TccStatus.EM_ANDAMENTO &&
                        !tcc.getOrientacoes().isEmpty())
                .collect(Collectors.toList());
    }

    @Override
    public List<TCC> buscarTccsParaFinalizar(Professor professor) {
        return db.values().stream()
                .filter(tcc -> tcc.getOrientador() != null &&
                        tcc.getOrientador().getId().equals(professor.getId()) &&
                        tcc.getStatus() == TccStatus.AGUARDANDO_BANCA)
                .collect(Collectors.toList());
    }
}