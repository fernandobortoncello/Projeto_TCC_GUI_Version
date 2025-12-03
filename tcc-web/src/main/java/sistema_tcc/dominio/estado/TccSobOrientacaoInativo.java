package sistema_tcc.dominio.estado;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ORIENTACAO_INATIVO")
public class TccSobOrientacaoInativo extends EstadoTcc {
    public TccSobOrientacaoInativo() { this.finalizar(); }
}