package sistema_tcc.view;
public class OrientacaoSnapshot {
    public final String data, texto;
    public OrientacaoSnapshot(String d, String t) { this.data = d; this.texto = t; }
    @Override public String toString() { return data + ": " + texto; }
}