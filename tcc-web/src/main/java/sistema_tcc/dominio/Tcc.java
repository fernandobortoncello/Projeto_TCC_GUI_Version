package sistema_tcc.dominio;

import jakarta.persistence.*;
import sistema_tcc.dominio.estado.EstadoTcc;
import sistema_tcc.dominio.estado.TccCriadoAtivo;
import sistema_tcc.dominio.estado.TccFinalizado;
import sistema_tcc.dto.TccDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
public class Tcc {

    @Id
    private String id;
    private String titulo;
    private String descricao;

    @ManyToOne
    private Aluno autor;

    @ManyToOne
    private Professor orientador;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<EstadoTcc> historicoEstados = new ArrayList<>();

    @Transient
    private EstadoTcc estadoAtualCache;

    @ElementCollection
    private List<String> orientacoes = new ArrayList<>();

    @ManyToMany
    private List<Professor> bancaMembros = new ArrayList<>();
    private String dataBanca;

    private Double notaFinal;
    private String parecerFinal;

    protected Tcc() {}

    public Tcc(Aluno autor, String titulo, String descricao) {
        this.id = UUID.randomUUID().toString();
        this.autor = autor;
        this.titulo = titulo;
        this.descricao = descricao;
        this.mudarEstado(new TccCriadoAtivo());
    }

    // --- Gestão de Estado ---
    public void mudarEstado(EstadoTcc novoEstado) {
        this.historicoEstados.add(novoEstado);
        this.estadoAtualCache = novoEstado;
    }

    public EstadoTcc getEstado() {
        if (estadoAtualCache == null && !historicoEstados.isEmpty()) {
            estadoAtualCache = historicoEstados.get(historicoEstados.size() - 1);
        }
        return estadoAtualCache;
    }

    // --- Métodos de Negócio (Delegam para o Estado) ---
    public void aceitarOrientador(Professor p) { getEstado().aceitarOrientador(this, p); }
    public void receberOrientacao(Professor p, String texto) { getEstado().registrarOrientacao(this, p, texto); }
    public void agendarBanca(Professor p, List<Professor> membros, String data) { getEstado().agendarBanca(this, p, membros, data); }
    public void receberNotaFinal(Professor p, double nota, String parecer) { getEstado().receberNotaFinal(this, p, nota, parecer); }

    // --- Métodos Auxiliares (Setters protegidos/package-private se possível, ou públicos apenas para o State) ---
    public void setOrientador(Professor p) { this.orientador = p; }
    public void adicionarTextoOrientacao(String t) { this.orientacoes.add(t); }
    public void setDadosBanca(List<Professor> m, String d) { this.bancaMembros = m; this.dataBanca = d; }
    public void setDadosFinais(double n, String p) { this.notaFinal = n; this.parecerFinal = p; }

    public boolean ehOrientadoPor(Professor p) {
        return this.orientador != null && this.orientador.getId().equals(p.getId());
    }

    public boolean isAtivo() {
        return !(getEstado() instanceof TccFinalizado) && getEstado().isAtivo();
    }

    // --- EXPORTAÇÃO DE DADOS (Substitui a extração externa via Getters) ---

    public TccDTO gerarSnapshot() {
        // Lógica de apresentação do status
        String statusLegivel = "PROPOSTA";
        if (getEstado() != null) {
            String nome = getEstado().getNomeEstado();
            if (nome.contains("Aceito") || nome.contains("Orientacao")) statusLegivel = "EM_ANDAMENTO";
            else if (nome.contains("Banca")) statusLegivel = "AGUARDANDO_BANCA";
            else if (nome.contains("Finalizado")) statusLegivel = "FINALIZADO";
        }

        return new TccDTO(
                this.id,
                this.titulo,
                this.descricao,
                statusLegivel,
                this.autor.getNome(),
                this.orientador != null ? this.orientador.getNome() : "Aguardando Orientador",
                new ArrayList<>(this.orientacoes),
                this.notaFinal,
                this.parecerFinal,
                this.dataBanca,
                this.bancaMembros.stream().map(Usuario::getNome).collect(Collectors.toList())
        );
    }

    public String getStatusNome() {
        return getEstado() != null ? getEstado().getNomeEstado() : "Desconhecido";
    }

    // Getters restritos (usados apenas pelo AtaGenerator ou JPA)
    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public Aluno getAutor() { return autor; }
    public Professor getOrientador() { return orientador; }
    public String getDataBanca() { return dataBanca; }
    public List<Professor> getBancaMembros() { return bancaMembros; }
    public Double getNotaFinal() { return notaFinal; }
    public String getParecerFinal() { return parecerFinal; }
}