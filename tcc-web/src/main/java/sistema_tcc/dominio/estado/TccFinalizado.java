package sistema_tcc.dominio.estado;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("FINALIZADO")
public class TccFinalizado extends EstadoTcc {
    // Estado final
}