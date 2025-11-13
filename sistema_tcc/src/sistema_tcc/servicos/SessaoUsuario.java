package sistema_tcc.servicos;

import sistema_tcc.dominio.Usuario;

/**
 * Gerencia o estado de login do usuário (Singleton).
 * Esta classe mantém qual usuário está logado na aplicação.
 */
public class SessaoUsuario {

    private Usuario usuarioLogado;

    /**
     * Define o usuário logado (chamado pelo AuthServico).
     */
    public void setUsuarioLogado(Usuario usuario) {
        this.usuarioLogado = usuario;
    }

    /**
     * Remove o usuário da sessão.
     */
    public void logout() {
        this.usuarioLogado = null;
    }

    /**
     * Verifica se há um usuário logado e se ele é do tipo esperado.
     * Este método é genérico e mais seguro.
     */
    public <T extends Usuario> T getUsuarioLogado(Class<T> tipo) {
        if (usuarioLogado == null) {
            throw new IllegalStateException("Nenhum usuário está logado.");
        }
        if (!tipo.isInstance(usuarioLogado)) {
            throw new IllegalStateException("O usuário logado não é do tipo " + tipo.getSimpleName());
        }
        return tipo.cast(usuarioLogado);
    }
}