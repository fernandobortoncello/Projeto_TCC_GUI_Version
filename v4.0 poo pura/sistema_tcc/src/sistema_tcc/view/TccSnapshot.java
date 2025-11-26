package sistema_tcc.view;
import java.util.List;

// Dados frios, mortos e seguros para a UI pintar
public class TccSnapshot {
    public final String id, titulo, autor, status, orientador, bancaInfo, nota, veredito, correcoes;
    public final List<OrientacaoSnapshot> orientacoes;
    public final List<String> membrosBanca;

    public TccSnapshot(String id, String titulo, String autor, String status, String orientador,
                       List<OrientacaoSnapshot> orientacoes, String bancaInfo, List<String> membrosBanca,
                       String nota, String veredito, String correcoes) {
        this.id = id; this.titulo = titulo; this.autor = autor; this.status = status;
        this.orientador = orientador; this.orientacoes = orientacoes; this.bancaInfo = bancaInfo;
        this.membrosBanca = membrosBanca; this.nota = nota; this.veredito = veredito; this.correcoes = correcoes;
    }
    @Override public String toString() { return titulo + " (" + autor + ")"; }
}