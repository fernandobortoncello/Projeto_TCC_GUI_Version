package sistema_tcc.dominio;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

/**
 * Representa a banca avaliadora (UC4).
 * Esta classe é IMUTÁVEL.
 */
public class Banca {
    private final List<Professor> membros;
    private final LocalDate dataApresentacao;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");

    public Banca(List<Professor> membros, LocalDate dataApresentacao) {
        if(membros == null || membros.size() < 2) {
            throw new IllegalArgumentException("A banca deve ter pelo menos 2 membros.");
        }
        // Cria uma CÓPIA defensiva da lista
        this.membros = List.copyOf(membros);
        this.dataApresentacao = dataApresentacao;
    }

    /**
     * GETTER SEGURO PARA COLEÇÃO.
     * Retorna uma lista não modificável.
     */
    public List<Professor> getMembros() {
        return Collections.unmodifiableList(this.membros);
    }

    public LocalDate getDataApresentacao() {
        return dataApresentacao;
    }

    public String getDataApresentacaoFormatada() {
        // Em um app real, dataApresentacao seria LocalDateTime
        return dataApresentacao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    @Override
    public String toString() {
        return "Banca em " + getDataApresentacaoFormatada() + " com " + membros.size() + " membros";
    }
}