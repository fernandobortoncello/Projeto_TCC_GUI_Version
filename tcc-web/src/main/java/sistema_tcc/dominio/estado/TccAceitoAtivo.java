package sistema_tcc.dominio.estado;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import sistema_tcc.dominio.Professor;
import sistema_tcc.dominio.Tcc;

@Entity
@DiscriminatorValue("ACEITO_ATIVO")
public class TccAceitoAtivo extends EstadoTcc {
    @Override
    public void registrarOrientacao(Tcc context, Professor p, String texto) {
        this.finalizar();
        context.mudarEstado(new TccAceitoInativo());

        TccSobOrientacaoAtivo novo = new TccSobOrientacaoAtivo();
        context.mudarEstado(novo);

        novo.registrarOrientacao(context, p, texto);
    }
}