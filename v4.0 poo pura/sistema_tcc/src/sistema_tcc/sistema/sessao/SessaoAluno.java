package sistema_tcc.sistema.sessao;

import sistema_tcc.dominio.Aluno;
import sistema_tcc.dominio.Tcc;
import sistema_tcc.sistema.Repositorio;
import sistema_tcc.view.TccSnapshot;

/**
 * O "Mundo" visto pelos olhos de um Aluno.
 * Só expõe métodos que o aluno pode executar.
 */
public class SessaoAluno implements Sessao {
    private final Aluno ator;
    private final Repositorio<Tcc> biblioteca;

    public SessaoAluno(Aluno ator, Repositorio<Tcc> biblioteca) {
        this.ator = ator;
        this.biblioteca = biblioteca;
    }

    @Override
    public String lerNomeUsuario() {
        return ator.lerNome();
    }

    public void submeterProposta(String titulo, String descricao) {
        // O aluno cria o TCC
        Tcc novoTcc = ator.criarProposta(titulo, descricao);
        // E o coloca na biblioteca do sistema
        biblioteca.guardar(novoTcc);
    }

    public TccSnapshot visualizarMeuTrabalho() {
        Tcc tcc = biblioteca.buscar(t -> t.pertenceA(ator));
        return (tcc != null) ? tcc.fotografar() : null;
    }
}