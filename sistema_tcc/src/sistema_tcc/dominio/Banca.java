package sistema_tcc.dominio;

import java.time.LocalDate;
import java.util.List;

// UC4
public class Banca {
    private List<Professor> membros;
    private LocalDate dataApresentacao;
    public Banca(List<Professor> membros, LocalDate data) {
        this.membros = membros;
        this.dataApresentacao = data;
    }
    @Override
    public String toString() {
        return "Banca em: " + dataApresentacao + " com " + membros.size() + " membros.";
    }
}