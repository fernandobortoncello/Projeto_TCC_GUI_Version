package sistema_tcc.dominio;

import sistema_tcc.dominio.tipo.TccStatus;
import sistema_tcc.view.*;
import java.util.*;
import java.util.stream.Collectors;

public class Tcc {
    private final String id;
    private final String titulo;
    private final String descricao;
    private TccStatus status;

    private final Aluno autor;
    private Professor orientador;
    private final List<Orientacao> orientacoes = new ArrayList<>();
    private Banca banca;
    private Avaliacao avaliacao;

    public Tcc(Aluno autor, String titulo, String descricao) {
        this.id = UUID.randomUUID().toString();
        this.autor = autor;
        this.titulo = titulo;
        this.descricao = descricao;
        this.status = TccStatus.PROPOSTA;
    }

    // --- MENSAGENS DE COMPORTAMENTO ---

    public void aceitarOrientador(Professor p) {
        if (this.status != TccStatus.PROPOSTA) throw new IllegalStateException("Não é proposta.");
        this.orientador = p;
        this.status = TccStatus.EM_ANDAMENTO;
    }

    public void receberOrientacao(Professor p, Orientacao o) {
        validarAutoridade(p);
        if (this.status != TccStatus.EM_ANDAMENTO) throw new IllegalStateException("TCC não em andamento.");
        this.orientacoes.add(o);
    }

    public void montarBanca(Professor p, Banca b) {
        validarAutoridade(p);
        this.banca = b;
        this.status = TccStatus.AGUARDANDO_BANCA;
    }

    public void receberNotaFinal(Professor p, Avaliacao a) {
        validarAutoridade(p);
        this.avaliacao = a;
        this.status = TccStatus.FINALIZADO;
    }

    private void validarAutoridade(Professor p) {
        if (orientador == null || !orientador.identificar(p.lerId()))
            throw new IllegalStateException("Acesso não autorizado: Não é o orientador.");
    }

    // --- MENSAGENS DE CONSULTA (Predicados) ---

    public boolean identificar(String id) { return this.id.equals(id); }
    public boolean pertenceA(Aluno a) { return this.autor.identificar(a.lerId()); }
    public boolean estaDisponivel() { return this.status == TccStatus.PROPOSTA; }
    public boolean ehOrientadoPor(Professor p) { return this.orientador != null && this.orientador.identificar(p.lerId()); }
    public boolean podeMarcarBanca(Professor p) { return ehOrientadoPor(p) && status == TccStatus.EM_ANDAMENTO && !orientacoes.isEmpty(); }
    public boolean podeSerAvaliado(Professor p) { return ehOrientadoPor(p) && status == TccStatus.AGUARDANDO_BANCA; }

    // --- EXPORTAÇÃO DE ESTADO (SNAPSHOT) ---

    public TccSnapshot fotografar() {
        String nomeOrientador = (orientador != null) ? orientador.lerNome() : "Aguardando";
        List<OrientacaoSnapshot> listaOri = orientacoes.stream().map(Orientacao::fotografar).collect(Collectors.toList());

        String bancaStr = (banca != null) ? banca.toString() : "Não definida";
        List<String> bancaMembros = (banca != null) ? banca.nomesMembros() : List.of();

        String nota = (avaliacao != null) ? String.valueOf(avaliacao.lerNota()) : "";
        String statusAv = (avaliacao != null) ? avaliacao.lerVeredito() : "";
        String correcoes = (avaliacao != null) ? avaliacao.lerParecer() : "";

        return new TccSnapshot(id, titulo, autor.lerNome(), status.name(), nomeOrientador, listaOri, bancaStr, bancaMembros, nota, statusAv, correcoes);
    }
}