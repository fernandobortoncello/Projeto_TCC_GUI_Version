package sistema_tcc.dominio;
import sistema_tcc.view.OrientacaoSnapshot;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Orientacao {
    private final LocalDate data;
    private final String texto;
    public Orientacao(LocalDate d, String t) { this.data = d; this.texto = t; }

    public OrientacaoSnapshot fotografar() {
        return new OrientacaoSnapshot(data.format(DateTimeFormatter.ofPattern("dd/MM")), texto);
    }
}