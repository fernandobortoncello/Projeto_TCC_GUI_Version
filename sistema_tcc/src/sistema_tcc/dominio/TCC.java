package sistema_tcc.dominio;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidade de Domínio Principal (Information Expert).
 * Contém a lógica de negócio (regras) sobre si mesma.
 */
public class TCC {
    private String id;
    private String titulo;
    private String descricao;
    private List<AreaConhecimento> areas;
    private TccStatus status;

    private Aluno autor;
    private Professor orientador;
    private Banca banca;
    private List<Orientacao> orientacoes;

    /**
     * Construtor para UC1: Cadastro de Tema.
     * Espera uma List<AreaConhecimento>
     */
    public TCC(Aluno autor, String titulo, String descricao, List<AreaConhecimento> areas) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("Título não pode ser vazio.");
        }
        if (autor == null) {
            throw new IllegalArgumentException("TCC deve ter um autor.");
        }

        this.autor = autor;
        this.titulo = titulo;
        this.descricao = descricao;
        this.areas = (areas != null) ? areas : new ArrayList<>();
        this.status = TccStatus.PROPOSTA; // Estado inicial
        this.orientacoes = new ArrayList<>();
    }

    /**
     * Lógica de Negócio para UC2: Escolha Orientador.
     */
    public void atribuirOrientador(Professor professor) {
        if (this.status != TccStatus.PROPOSTA) {
            throw new IllegalStateException("O TCC não está mais na fase de proposta.");
        }
        if (professor == null) {
            throw new IllegalArgumentException("Professor não pode ser nulo.");
        }
        this.orientador = professor;
        this.status = TccStatus.EM_ANDAMENTO;
        System.out.println("LOG: Orientador " + professor.getNome() + " atribuído ao TCC: " + titulo);
    }

    // --- Getters e Setters (simplificados) ---

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public Aluno getAutor() {
        return autor;
    }

    public Professor getOrientador() {
        return orientador;
    }

    public TccStatus getStatus() {
        return status;
    }
}