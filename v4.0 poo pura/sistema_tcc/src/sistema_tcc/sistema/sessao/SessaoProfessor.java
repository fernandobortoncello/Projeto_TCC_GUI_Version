package sistema_tcc.sistema.sessao;

import sistema_tcc.dominio.*;
import sistema_tcc.sistema.Repositorio;
import sistema_tcc.view.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * O "Mundo" visto pelos olhos de um Professor.
 */
public class SessaoProfessor implements Sessao {
    private final Professor ator;
    private final Repositorio<Tcc> biblioteca;
    private final Repositorio<Usuario> comunidade;

    public SessaoProfessor(Professor ator, Repositorio<Tcc> biblioteca, Repositorio<Usuario> comunidade) {
        this.ator = ator;
        this.biblioteca = biblioteca;
        this.comunidade = comunidade;
    }

    @Override
    public String lerNomeUsuario() { return ator.lerNome(); }

    // --- UC2: Escolher Tema ---
    public List<TccSnapshot> verPropostasDisponiveis() {
        return biblioteca.filtrar(Tcc::estaDisponivel)
                .stream().map(Tcc::fotografar).collect(Collectors.toList());
    }

    public void assumirOrientacao(String idTcc) {
        Tcc tcc = buscarTccReal(idTcc);
        if (tcc != null) ator.orientar(tcc);
    }

    // --- UC3: Orientação ---
    public List<TccSnapshot> verMeusOrientandos() {
        return biblioteca.filtrar(t -> t.ehOrientadoPor(ator))
                .stream().map(Tcc::fotografar).collect(Collectors.toList());
    }

    public void registrarOrientacao(String idTcc, LocalDate data, String texto) {
        Tcc tcc = buscarTccReal(idTcc);
        if (tcc != null) ator.registrarProgresso(tcc, data, texto);
    }

    // --- UC4: Banca ---
    public List<TccSnapshot> verTccsProntosParaBanca() {
        return biblioteca.filtrar(t -> t.podeMarcarBanca(ator))
                .stream().map(Tcc::fotografar).collect(Collectors.toList());
    }

    public List<ProfessorSnapshot> listarColegas() {
        return comunidade.filtrar(u -> u instanceof Professor)
                .stream().map(u -> ((Professor)u).fotografar())
                .collect(Collectors.toList());
    }

    public void agendarBanca(String idTcc, LocalDate data, List<String> idsColegas) {
        Tcc tcc = buscarTccReal(idTcc);
        List<Professor> membros = comunidade.filtrar(u -> idsColegas.contains(u.lerId()) && u instanceof Professor)
                .stream().map(u -> (Professor)u).collect(Collectors.toList());

        if (tcc != null) ator.agendarBanca(tcc, membros, data);
    }

    // --- UC5: Finalizar ---
    public List<TccSnapshot> verTccsParaAvaliar() {
        return biblioteca.filtrar(t -> t.podeSerAvaliado(ator))
                .stream().map(Tcc::fotografar).collect(Collectors.toList());
    }

    public void avaliarFinal(String idTcc, double nota, String parecer) {
        Tcc tcc = buscarTccReal(idTcc);
        if (tcc != null) ator.avaliarDefesa(tcc, nota, parecer);
    }

    // Helper privado: O professor interage com o objeto real, a UI vê o Snapshot
    private Tcc buscarTccReal(String id) {
        return biblioteca.buscar(t -> t.identificar(id));
    }
}