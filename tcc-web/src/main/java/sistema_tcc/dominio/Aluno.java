package sistema_tcc.dominio;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ALUNO")
public class Aluno extends Usuario {
    protected Aluno() {}
    public Aluno(String id, String nome, String senha) { super(id, nome, senha); }

    public Tcc criarProposta(String titulo, String descricao) {
        return new Tcc(this, titulo, descricao);
    }
}