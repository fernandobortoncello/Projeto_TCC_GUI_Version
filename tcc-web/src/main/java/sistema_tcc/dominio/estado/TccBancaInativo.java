package sistema_tcc.dominio.estado;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("BANCA_INATIVO")
public class TccBancaInativo extends EstadoTcc {
    public TccBancaInativo() { this.finalizar(); }
}