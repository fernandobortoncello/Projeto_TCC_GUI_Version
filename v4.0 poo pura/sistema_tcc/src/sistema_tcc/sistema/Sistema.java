package sistema_tcc.sistema;

import sistema_tcc.dominio.*;
import sistema_tcc.dominio.tipo.Papel;
import sistema_tcc.sistema.sessao.*;

/**
 * O "Mundo" onde os objetos vivem.
 * Em OO Puro, o sistema é apenas um container de objetos vivos.
 */
public class Sistema {

    private final Repositorio<Usuario> usuarios = new Repositorio<>();
    private final Repositorio<Tcc> tccs = new Repositorio<>();

    public Sistema() {
        inicializarVida();
    }

    private void inicializarVida() {
        // Criar objetos vivos e persistentes na memória
        usuarios.guardar(new Aluno("2025001", "Aline de Abreu Espindola", "senha123"));
        usuarios.guardar(new Aluno("2025002", "Fernando Bortoncello", "senha123"));

        usuarios.guardar(new Professor("111222", "Willian Bolzan", "prof123"));
        usuarios.guardar(new Professor("333444", "Thiago Martins Oliveira", "prof123"));
        usuarios.guardar(new Professor("555666", "Tiago J. G. Carvalho", "prof123", Papel.COORDENADOR));
    }

    /**
     * Tenta estabelecer uma conexão (Sessão) com o sistema.
     * Retorna uma Sessão Polimórfica (Aluno ou Professor).
     */
    public Sessao solicitarAcesso(String id, String senha) {
        Usuario u = usuarios.buscar(user -> user.identificar(id));

        if (u != null && u.autenticar(senha)) {
            // Factory Method baseado no tipo do objeto
            if (u instanceof Aluno) {
                return new SessaoAluno((Aluno) u, tccs);
            } else if (u instanceof Professor) {
                return new SessaoProfessor((Professor) u, tccs, usuarios);
            }
        }
        throw new IllegalArgumentException("Credenciais inválidas.");
    }
}