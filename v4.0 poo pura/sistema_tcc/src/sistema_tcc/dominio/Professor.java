package sistema_tcc.dominio;
import sistema_tcc.dominio.tipo.Papel;
import sistema_tcc.view.ProfessorSnapshot;
import java.time.LocalDate;
import java.util.List;

public class Professor extends Usuario {
    public Professor(String cpf, String nome, String senha) { super(cpf, nome, senha, Papel.PROFESSOR); }
    public Professor(String cpf, String nome, String senha, Papel p) { super(cpf, nome, senha, p); }

    // O Professor "age" sobre o TCC
    public void orientar(Tcc tcc) {
        tcc.aceitarOrientador(this);
    }

    public void registrarProgresso(Tcc tcc, LocalDate data, String texto) {
        tcc.receberOrientacao(this, new Orientacao(data, texto));
    }

    public void agendarBanca(Tcc tcc, List<Professor> membros, LocalDate data) {
        tcc.montarBanca(this, new Banca(membros, data));
    }

    public void avaliarDefesa(Tcc tcc, double nota, String parecer) {
        tcc.receberNotaFinal(this, new Avaliacao(nota, parecer));
    }

    public ProfessorSnapshot fotografar() {
        return new ProfessorSnapshot(id, nome);
    }
}