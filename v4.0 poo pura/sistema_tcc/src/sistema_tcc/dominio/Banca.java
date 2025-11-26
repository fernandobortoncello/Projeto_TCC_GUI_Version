package sistema_tcc.dominio;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class Banca {
    private final List<Professor> membros;
    private final LocalDate data;
    public Banca(List<Professor> m, LocalDate d) { this.membros = m; this.data = d; }

    public List<String> nomesMembros() {
        return membros.stream().map(Professor::lerNome).collect(Collectors.toList());
    }
    @Override public String toString() { return "Data: " + data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")); }
}