package sistema_tcc.servicos;

/**
 * Exceção customizada para falhas de login (RNF002).
 */
public class AuthException extends Exception {
    public AuthException(String message) {
        super(message);
    }
}