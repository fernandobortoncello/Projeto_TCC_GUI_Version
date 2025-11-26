package sistema_tcc.view;
public class ProfessorSnapshot {
    public final String id, nome;
    public ProfessorSnapshot(String id, String nome) { this.id = id; this.nome = nome; }
    @Override public String toString() { return nome; }
}