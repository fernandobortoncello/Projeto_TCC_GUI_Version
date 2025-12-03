package sistema_tcc.dominio.estado;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ACEITO_INATIVO")
public class TccAceitoInativo extends EstadoTcc {
    public TccAceitoInativo() { this.finalizar(); }
}