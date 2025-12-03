package sistema_tcc.dominio.estado;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import sistema_tcc.dominio.Professor;
import sistema_tcc.dominio.Tcc;

@Entity
@DiscriminatorValue("CRIADO_ATIVO")
public class TccCriadoAtivo extends EstadoTcc {
    @Override
    public void aceitarOrientador(Tcc context, Professor p) {
        this.finalizar();
        context.mudarEstado(new TccCriadoInativo());

        TccAceitoAtivo novo = new TccAceitoAtivo();
        context.mudarEstado(novo);

        context.setOrientador(p);
    }
}