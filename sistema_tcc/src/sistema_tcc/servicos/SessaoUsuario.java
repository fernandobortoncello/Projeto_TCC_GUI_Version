package sistema_tcc.servicos;

import sistema_tcc.dominio.Usuario;

/**
 * Singleton para gerenciar o estado da sessão (quem está logado).
 * CORRIGIDO: Adicionados os métodos setUsuarioLogado() e logout().
 */
public class SessaoUsuario {

    private static SessaoUsuario instancia;
    private Usuario usuarioLogado;

    // Construtor privado para Singleton
    private SessaoUsuario() {}

    /**
     * Ponto de acesso global para a sessão.
     */
    public static SessaoUsuario getInstancia() {
        if (instancia == null) {
            instancia = new SessaoUsuario();
        }
        return instancia;
    }

    /**
     * CORREÇÃO: Método que faltava, chamado pelo LoginControlador.
     */
    public void setUsuarioLogado(Usuario usuario) {
        this.usuarioLogado = usuario;
    }

    /**
     * CORREÇÃO: Método que faltava, chamado pelos botões de logout.
     */
    public void logout() {
        this.usuarioLogado = null;
    }

    /**
     * Método básico para obter o usuário.
     */
    public Usuario getUsuarioLogado() {
        return this.usuarioLogado;
    }

    /**
     * Método seguro para obter o usuário e já verificar seu tipo (Papel).
     * Usado pelos controladores de Aluno e Professor.
     */
    public <T extends Usuario> T getUsuarioLogado(Class<T> tipo) {
        if (usuarioLogado == null) {
            throw new IllegalStateException("Nenhum usuário está logado.");
        }
        if (!tipo.isInstance(usuarioLogado)) {
            throw new IllegalStateException("O usuário logado não é do tipo esperado (" + tipo.getSimpleName() + ").");
        }
        return tipo.cast(usuarioLogado);
    }
}