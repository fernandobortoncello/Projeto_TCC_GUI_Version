package sistema_tcc.dominio.estado;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import sistema_tcc.dominio.Professor;
import sistema_tcc.dominio.Tcc;
import java.util.List;

@Entity
@DiscriminatorValue("ORIENTACAO_ATIVO")
public class TccSobOrientacaoAtivo extends EstadoTcc {
    @Override
    public void registrarOrientacao(Tcc context, Professor p, String texto) {
        if (!context.ehOrientadoPor(p)) throw new IllegalStateException("Apenas orientador.");
        context.adicionarTextoOrientacao(texto);
    }

    @Override
    public void agendarBanca(Tcc context, Professor p, List<Professor> membros, String data) {
        if (!context.ehOrientadoPor(p)) throw new IllegalStateException("Apenas orientador.");

        this.finalizar();
        context.mudarEstado(new TccSobOrientacaoInativo());

        TccBancaAtivo novo = new TccBancaAtivo();
        context.mudarEstado(novo);

        context.setDadosBanca(membros, data);
    }
}