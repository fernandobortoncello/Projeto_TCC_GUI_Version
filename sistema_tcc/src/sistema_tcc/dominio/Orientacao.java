package sistema_tcc.dominio;

import java.time.LocalDate;

public class Orientacao {
    private LocalDate data;
    private String descricao;
    public Orientacao(LocalDate data, String descricao) {
        this.data = data;
        this.descricao = descricao;
    }
    @Override
    public String toString() {
        return data.toString() + ": " + descricao;
    }
}