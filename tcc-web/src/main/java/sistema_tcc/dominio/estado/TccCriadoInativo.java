package sistema_tcc.dominio.estado;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CRIADO_INATIVO")
public class TccCriadoInativo extends EstadoTcc {
    public TccCriadoInativo() { this.finalizar(); }
}