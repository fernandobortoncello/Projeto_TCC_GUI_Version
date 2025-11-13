package sistema_tcc.servicos;

import sistema_tcc.dominio.Aluno;
import sistema_tcc.dominio.AreaConhecimento;
import sistema_tcc.dominio.Professor;
import sistema_tcc.dominio.TCC;
import sistema_tcc.dominio.TccStatus;
import sistema_tcc.repositorio.TccRepositorio;

import java.util.List;

/**
 * Serviço (Controlador GRASP) para os casos de uso de TCC.
 * Orquestra as ações entre a UI e as entidades de domínio.
 */
public class TccServico {

    private final TccRepositorio tccRepositorio;
    // private final EmailServico emailServico; // Para RF006, RF014, etc.

    public TccServico(TccRepositorio tccRepositorio) {
        this.tccRepositorio = tccRepositorio;
        // this.emailServico = new EmailServico();
    }

    /**
     * Implementa UC1: Cadastro de Tema (RF002)
     * A assinatura espera uma List<AreaConhecimento>
     */
    public TCC cadastrarTema(Aluno autor, String titulo, String desc, List<AreaConhecimento> areas) {
        // A lógica de negócio (criação) está na própria entidade TCC.
        TCC novoTCC = new TCC(autor, titulo, desc, areas);

        // O repositório salva a nova entidade
        return tccRepositorio.salvar(novoTCC);
    }

    /**
     * Implementa UC2: Escolha Orientador (RF005)
     */
    public void escolherOrientador(Professor professor, TCC tcc) {
        // O serviço ORQUESTRA. A entidade TCC executa a lógica de negócio.
        tcc.atribuirOrientador(professor);

        // Salva o estado atualizado
        tccRepositorio.salvar(tcc);

        // Dispara eventos (simulado)
        // emailServico.enviarEmail(tcc.getAutor(), "Orientador Definido", ...); // RF006
        System.out.println("RELATÓRIO: Prof. " + professor.getNome() + " agora orienta " + tcc.getTitulo()); // RF007
    }

    /**
     * Implementa RF004: Professor consultar temas.
     */
    public List<TCC> listarTemasPropostos() {
        return tccRepositorio.buscarPorStatus(TccStatus.PROPOSTA);
    }
}