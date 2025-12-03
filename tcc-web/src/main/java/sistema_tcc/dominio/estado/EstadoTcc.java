package sistema_tcc.dominio.estado;

import jakarta.persistence.*;
import sistema_tcc.dominio.Professor;
import sistema_tcc.dominio.Tcc;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_estado")
public abstract class EstadoTcc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    protected LocalDateTime dataCriacao;
    protected LocalDateTime dataFinalizacao;

    protected EstadoTcc() {
        this.dataCriacao = LocalDateTime.now();
    }

    public void finalizar() {
        this.dataFinalizacao = LocalDateTime.now();
    }

    public boolean isAtivo() {
        return dataFinalizacao == null;
    }

    // Métodos de Negócio (Default: Lançam exceção)
    public void aceitarOrientador(Tcc context, Professor p) {
        throw new IllegalStateException("Ação inválida no estado atual: " + getNomeEstado());
    }

    public void registrarOrientacao(Tcc context, Professor p, String texto) {
        throw new IllegalStateException("Ação inválida no estado atual: " + getNomeEstado());
    }

    public void agendarBanca(Tcc context, Professor p, List<Professor> membros, String data) {
        throw new IllegalStateException("Ação inválida no estado atual: " + getNomeEstado());
    }

    public void receberNotaFinal(Tcc context, Professor p, double nota, String parecer) {
        throw new IllegalStateException("Ação inválida no estado atual: " + getNomeEstado());
    }

    public String getNomeEstado() {
        return this.getClass().getSimpleName();
    }
}