package sistema_tcc.dominio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Entidade principal (Aggregate Root).
 * Esta classe é o "Information Expert" (GRASP).
 * NENHUM setter público. Getters de lista são protegidos.
 */
public class TCC {

    private String id;
    private String titulo;
    private String descricao;
    private List<AreaConhecimento> areas;
    private TccStatus status;

    private final Aluno autor;
    private Professor orientador;
    private Banca banca;
    private Avaliacao avaliacaoFinal;

    // A lista interna é FINAL e PRIVADA.
    private final List<Orientacao> orientacoes;

    /**
     * Construtor para UC1: Cadastro de Tema.
     * Garante que o objeto nasce em estado válido.
     */
    public TCC(Aluno autor, String titulo, String descricao, List<AreaConhecimento> areas) {
        this.autor = autor;
        this.titulo = titulo;
        this.descricao = descricao;
        // Cria uma CÓPIA defensiva da lista de áreas
        this.areas = new ArrayList<>(areas);
        this.status = TccStatus.PROPOSTA; // Estado inicial
        this.orientacoes = new ArrayList<>();
    }

    // --- MÉTODOS DE COMPORTAMENTO (Substituem Setters) ---

    /**
     * Lógica de Negócio para UC2: Escolha Orientador.
     */
    public void atribuirOrientador(Professor professor) {
        if (this.status != TccStatus.PROPOSTA) {
            throw new IllegalStateException("O TCC não está mais na fase de proposta.");
        }
        this.orientador = professor;
        this.status = TccStatus.EM_ANDAMENTO;
    }

    /**
     * Lógica de Negócio para UC3: Controle de Orientação.
     * Este é o único método que pode adicionar uma orientação.
     */
    public void adicionarOrientacao(Professor professor, Orientacao orientacao) {
        // RNF-SEG02: Validação de regra de negócio
        if (this.orientador == null || !this.orientador.getId().equals(professor.getId())) {
            throw new IllegalStateException("Apenas o orientador oficial pode registrar uma orientação.");
        }
        if (this.status != TccStatus.EM_ANDAMENTO) {
            throw new IllegalStateException("O TCC não está em andamento.");
        }
        this.orientacoes.add(orientacao);
    }

    /**
     * Lógica de Negócio para UC4: Escolha da Banca.
     */
    public void definirBanca(Banca banca) {
        if (this.status != TccStatus.EM_ANDAMENTO) {
            throw new IllegalStateException("O TCC deve estar 'Em Andamento' para definir a banca.");
        }
        if (this.orientacoes.isEmpty()) {
            throw new IllegalStateException("O TCC deve ter pelo menos uma orientação registrada.");
        }
        this.banca = banca;
        this.status = TccStatus.AGUARDANDO_BANCA;
    }

    /**
     * Lógica de Negócio para UC5: Finalização do TCC.
     */
    public void finalizarTCC(Avaliacao avaliacao) {
        if (this.status != TccStatus.AGUARDANDO_BANCA) {
            throw new IllegalStateException("O TCC deve estar 'Aguardando Banca' para ser finalizado.");
        }
        this.avaliacaoFinal = avaliacao;
        this.status = TccStatus.FINALIZADO;
    }

    // --- GETTERS SEGUROS (Safe Getters) ---

    // ID é necessário para o repositório
    public String getId() { return id; }
    // Usado apenas pelo MockRepositorio. Em JPA, seria injetado.
    public void setId(String id) { this.id = id; }

    // Getters de dados imutáveis (String, Enum, etc.) são seguros.
    public String getTitulo() { return titulo; }
    public Aluno getAutor() { return autor; }
    public Professor getOrientador() { return orientador; }
    public TccStatus getStatus() { return status; }
    public Banca getBanca() { return banca; } // Banca é imutável (campos 'final')
    public Avaliacao getAvaliacaoFinal() { return avaliacaoFinal; } // Avaliacao é imutável

    /**
     * GETTER SEGURO PARA COLEÇÃO (Encapsulamento de Coleção).
     * Retorna uma listaNÃO MODIFICÁVEL.
     * A UI pode ler, mas não pode adicionar/remover.
     */
    public List<Orientacao> getOrientacoes() {
        return Collections.unmodifiableList(this.orientacoes);
    }

    /**
     * GETTER SEGURO PARA COLEÇÃO.
     */
    public List<AreaConhecimento> getAreas() {
        return Collections.unmodifiableList(this.areas);
    }

    @Override
    public String toString() {
        return this.titulo + " (Aluno: " + this.autor.getNome() + ")";
    }
}