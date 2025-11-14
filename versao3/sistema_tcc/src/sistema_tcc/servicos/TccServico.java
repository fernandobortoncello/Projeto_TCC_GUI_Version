package sistema_tcc.servicos;

import sistema_tcc.dominio.*;
import sistema_tcc.repositorio.AlunoRepositorio;
import sistema_tcc.repositorio.ProfessorRepositorio;
import sistema_tcc.repositorio.TccRepositorio;
import sistema_tcc.servicos.dto.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço (Controlador GRASP) para os casos de uso de TCC.
 * Responsável por orquestrar e MAPEAR o domínio para DTOs.
 */
public class TccServico {

    private final TccRepositorio tccRepositorio;
    private final ProfessorRepositorio professorRepositorio;
    private final AlunoRepositorio alunoRepositorio;

    // (CORREÇÃO DE ERRO 6)
    public TccServico(TccRepositorio tccRepo, ProfessorRepositorio profRepo, AlunoRepositorio alunoRepo) {
        this.tccRepositorio = tccRepo;
        this.professorRepositorio = profRepo;
        this.alunoRepositorio = alunoRepo;
    }

    // --- Métodos para UC1 (Aluno) ---

    public void cadastrarTema(String idAluno, String titulo, String desc) {
        Aluno autor = alunoRepositorio.buscarPorMatricula(idAluno);
        if (autor == null) {
            throw new RuntimeException("Aluno não encontrado.");
        }

        // TODO: Buscar áreas de conhecimento da UI
        TCC novoTCC = new TCC(autor, titulo, desc, List.of(AreaConhecimento.ENGENHARIA_SOFTWARE));
        tccRepositorio.salvar(novoTCC);
    }

    // --- Métodos para UC2 (Professor) ---

    public List<TccResumoDTO> listarTemasPropostos() {
        return tccRepositorio.buscarPorStatus(TccStatus.PROPOSTA).stream()
                .map(TccServico::toTccResumoDTO)
                .collect(Collectors.toList());
    }

    public void escolherOrientador(String idProfessor, String idTCC) {
        Professor professor = professorRepositorio.buscarPorId(idProfessor);
        TCC tcc = tccRepositorio.buscarPorId(idTCC);

        if (professor == null || tcc == null) {
            throw new RuntimeException("Professor ou TCC não encontrado.");
        }

        tcc.atribuirOrientador(professor);
        tccRepositorio.salvar(tcc);
        System.out.println("RELATÓRIO: Prof. " + professor.getNome() + " agora orienta " + tcc.getTitulo()); // RF007
    }

    // --- Métodos para UC3 (Professor) ---

    public List<TccResumoDTO> listarTCCsOrientados(String idProfessor) {
        Professor professor = professorRepositorio.buscarPorId(idProfessor);
        return tccRepositorio.buscarPorOrientador(professor).stream()
                .map(TccServico::toTccResumoDTO)
                .collect(Collectors.toList());
    }

    public void registrarOrientacao(String idProfessor, String idTCC, LocalDate data, String descricao) {
        Professor professor = professorRepositorio.buscarPorId(idProfessor);
        TCC tcc = tccRepositorio.buscarPorId(idTCC);

        if (professor == null || tcc == null) {
            throw new RuntimeException("Professor ou TCC não encontrado.");
        }

        Orientacao orientacao = new Orientacao(data, descricao);
        tcc.adicionarOrientacao(professor, orientacao);
        tccRepositorio.salvar(tcc);
    }

    // --- Métodos para UC4 (Professor) ---

    public List<ProfessorDTO> listarProfessores() {
        return professorRepositorio.listarTodos().stream()
                .map(TccServico::toProfessorDTO)
                .collect(Collectors.toList());
    }

    public List<TccResumoDTO> listarTccsParaDefinirBanca(String idProfessor) {
        Professor professor = professorRepositorio.buscarPorId(idProfessor);
        return tccRepositorio.buscarTccsParaDefinirBanca(professor).stream()
                .map(TccServico::toTccResumoDTO)
                .collect(Collectors.toList());
    }

    public void definirBanca(String idTCC, List<String> idsMembros, LocalDate data) {
        TCC tcc = tccRepositorio.buscarPorId(idTCC);
        List<Professor> membros = idsMembros.stream()
                .map(professorRepositorio::buscarPorId)
                .collect(Collectors.toList());

        Banca banca = new Banca(membros, data);
        tcc.definirBanca(banca);
        tccRepositorio.salvar(tcc);
    }

    // --- Métodos para UC5 (Professor) ---

    public List<TccResumoDTO> listarTccsParaFinalizar(String idProfessor) {
        Professor professor = professorRepositorio.buscarPorId(idProfessor);
        return tccRepositorio.buscarTccsParaFinalizar(professor).stream()
                .map(TccServico::toTccResumoDTO)
                .collect(Collectors.toList());
    }

    public void finalizarTCC(String idTCC, double nota, String anotacoes) {
        TCC tcc = tccRepositorio.buscarPorId(idTCC);
        Avaliacao avaliacao = new Avaliacao(nota, anotacoes);
        tcc.finalizarTCC(avaliacao);
        tccRepositorio.salvar(tcc);
    }

    // --- Métodos para (Aluno) ---

    public TccDTO buscarTccDtoPorAluno(String idAluno) {
        Aluno aluno = alunoRepositorio.buscarPorMatricula(idAluno);
        if (aluno == null) {
            throw new RuntimeException("Aluno não encontrado para o ID: " + idAluno);
        }
        TCC tcc = tccRepositorio.buscarPorAluno(aluno);
        if (tcc == null) {
            return null; // Aluno ainda não cadastrou TCC
        }
        return toTccDTO(tcc); // Mapeia para o DTO completo
    }

    // --- MÉTODOS DE MAPEAMENTO (A "Mágica" do DTO) ---

    private static ProfessorDTO toProfessorDTO(Professor p) {
        return new ProfessorDTO(p.getId(), p.getNome());
    }

    private static TccResumoDTO toTccResumoDTO(TCC tcc) {
        return new TccResumoDTO(tcc.getId(), tcc.toString());
    }

    private static TccDTO toTccDTO(TCC tcc) {
        // Mapeia Sub-DTOs
        List<OrientacaoDTO> orientacoesDTO = tcc.getOrientacoes().stream()
                .map(o -> new OrientacaoDTO(o.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), o.getDescricao()))
                .collect(Collectors.toList());

        BancaDTO bancaDTO = null;
        if(tcc.getBanca() != null) {
            List<ProfessorDTO> membrosDTO = tcc.getBanca().getMembros().stream()
                    .map(TccServico::toProfessorDTO)
                    .collect(Collectors.toList());
            bancaDTO = new BancaDTO(tcc.getBanca().getDataApresentacaoFormatada(), membrosDTO);
        }

        AvaliacaoDTO avaliacaoDTO = null;
        if(tcc.getAvaliacaoFinal() != null) {
            avaliacaoDTO = new AvaliacaoDTO(
                    tcc.getAvaliacaoFinal().getNotaFinal(),
                    tcc.getAvaliacaoFinal().getAnotacoes(),
                    tcc.getAvaliacaoFinal().getStatus()
            );
        }

        String orientadorNome = (tcc.getOrientador() != null) ? tcc.getOrientador().getNome() : "(Aguardando definição)";

        // Cria o DTO principal
        return new TccDTO(
                tcc.getStatus().toString(),
                orientadorNome,
                orientacoesDTO,
                bancaDTO,
                avaliacaoDTO
        );
    }
}