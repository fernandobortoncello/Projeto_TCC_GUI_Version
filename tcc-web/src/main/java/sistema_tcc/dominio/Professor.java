package sistema_tcc.dominio;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PROFESSOR")
public class Professor extends Usuario {
    protected Professor() {}
    public Professor(String id, String nome, String senha) { super(id, nome, senha); }
}