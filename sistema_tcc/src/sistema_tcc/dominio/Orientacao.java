package sistema_tcc.dominio;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Representa uma sessão de orientação (UC3).
 */
public class Orientacao {
    private final LocalDate data;
    private final String descricao;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");


    public Orientacao(LocalDate data, String descricao) {
        if (data == null) {
            throw new IllegalArgumentException("Data não pode ser nula.");
        }
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("Descrição não pode ser vazia.");
        }
        this.data = data;
        this.descricao = descricao;
    }

    public LocalDate getData() {
        return data;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        // Formato amigável para a ListView do Aluno
        return data.format(FORMATTER) + " - " + descricao;
    }
}