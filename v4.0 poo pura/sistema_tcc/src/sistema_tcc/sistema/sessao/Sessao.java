package sistema_tcc.sistema.sessao;

/**
 * Contrato de interação.
 * Define o que QUALQUER usuário pode fazer (ex: logout).
 */
public interface Sessao {
    String lerNomeUsuario();
    // Logout é tratado na UI descartando a sessão
}