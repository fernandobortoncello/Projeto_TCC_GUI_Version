package sistema_tcc.servicos;

import sistema_tcc.dominio.*;
import sistema_tcc.repositorio.ProfessorRepositorio;
import sistema_tcc.repositorio.TccRepositorio;
import java.time.LocalDate;
import java.util.List;

/**
 * Serviço (Controlador GRASP) para os casos de uso de TCC.
 * Orquestra as ações entre a UI e as entidades de domínio.
 */
public class TccServico {

    private final TccRepositorio tccRepositorio;
    private final ProfessorRepositorio professorRepositorio;
    // private final EmailServico emailServico; // Para RF006, RF014, etc.

    public TccServico(TccRepositorio tccRepositorio, ProfessorRepositorio professorRepositorio) {
        this.tccRepositorio = tccRepositorio;
        this.professorRepositorio = professorRepositorio;
    }

    // --- Métodos para UC1 (Aluno) ---

    public TCC cadastrarTema(Aluno autor, String titulo, String desc, List<AreaConhecimento> areas) {
        TCC novoTCC = new TCC(autor, titulo, desc, areas);
        return tccRepositorio.salvar(novoTCC);
    }

    // --- Métodos para UC2 (Professor) ---

    public List<TCC> listarTemasPropostos() {
        return tccRepositorio.buscarPorStatus(TccStatus.PROPOSTA);
    }

    public void escolherOrientador(Professor professor, TCC tcc) {
        tcc.atribuirOrientador(professor);
        tccRepositorio.salvar(tcc);
        // emailServico.enviarEmail(tcc.getAutor(), "Orientador Definido", ...); // RF006
        System.out.println("RELATÓRIO: Prof. " + professor.getNome() + " agora orienta " + tcc.getTitulo()); // RF007
    }

    // --- Métodos para UC3 (Professor) ---

    public List<TCC> listarTCCsOrientados(Professor professor) {
        return tccRepositorio.buscarPorOrientador(professor);
    }

    public void registrarOrientacao(Professor professor, TCC tcc, LocalDate data, String descricao) {
        Orientacao orientacao = new Orientacao(data, descricao);
        tcc.adicionarOrientacao(professor, orientacao);
        tccRepositorio.salvar(tcc);
    }

    // --- Métodos para UC4 (Professor) ---

    public List<Professor> listarProfessores() {
        return professorRepositorio.listarTodos();
    }

    public List<TCC> listarTccsParaDefinirBanca(Professor professor) {
        return tccRepositorio.buscarTccsParaDefinirBanca(professor);
    }

    public void definirBanca(TCC tcc, List<Professor> membros, LocalDate data) {
        Banca banca = new Banca(membros, data);
        tcc.definirBanca(banca);
        tccRepositorio.salvar(tcc);
        // emailServico.enviarNotificacao(tcc.getAutor(), "Banca Definida", ...); // RF014
    }

    // --- Métodos para UC5 (Professor) ---

    public List<TCC> listarTccsParaFinalizar(Professor professor) {
        return tccRepositorio.buscarTccsParaFinalizar(professor);
    }

    public void finalizarTCC(TCC tcc, double nota, String anotacoes) {
        Avaliacao avaliacao = new Avaliacao(nota, anotacoes);
        tcc.finalizarTCC(avaliacao);
        tccRepositorio.salvar(tcc);
        // emailServico.enviarNotificacao(tcc.getAutor(), "TCC Finalizado", ...); // RF019
        // gerarAtaPDF(tcc); // RF020
    }

    // --- Métodos para (Aluno) ---

    public TCC buscarTccPorAluno(Aluno aluno) {
        return tccRepositorio.buscarPorAluno(aluno);
    }
}