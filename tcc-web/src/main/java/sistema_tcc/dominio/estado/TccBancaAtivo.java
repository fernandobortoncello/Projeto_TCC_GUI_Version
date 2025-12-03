package sistema_tcc.dominio.estado;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import sistema_tcc.dominio.Professor;
import sistema_tcc.dominio.Tcc;

@Entity
@DiscriminatorValue("BANCA_ATIVO")
public class TccBancaAtivo extends EstadoTcc {
    @Override
    public void receberNotaFinal(Tcc context, Professor p, double nota, String parecer) {
        if (!context.ehOrientadoPor(p)) throw new IllegalStateException("Apenas orientador.");

        this.finalizar();
        context.mudarEstado(new TccBancaInativo());

        TccFinalizado fim = new TccFinalizado();
        context.mudarEstado(fim);

        context.setDadosFinais(nota, parecer);
    }
}