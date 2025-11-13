package sistema_tcc.dominio;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Representa a banca avaliadora (UC4).
 */
public class Banca {
    private List<Professor> membros;
    private LocalDate dataApresentacao;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");

    public Banca(List<Professor> membros, LocalDate dataApresentacao) {
        if(membros == null || membros.size() < 2) {
            throw new IllegalArgumentException("A banca deve ter pelo menos 2 membros.");
        }
        this.membros = membros;
        this.dataApresentacao = dataApresentacao;
    }

    // Getters
    public List<Professor> getMembros() { return membros; }
    public LocalDate getDataApresentacao() { return dataApresentacao; }

    public String getDataApresentacaoFormatada() {
        // Em um app real, dataApresentacao seria LocalDateTime
        return dataApresentacao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    @Override
    public String toString() {
        return "Banca em " + getDataApresentacaoFormatada() + " com " + membros.size() + " membros";
    }
}