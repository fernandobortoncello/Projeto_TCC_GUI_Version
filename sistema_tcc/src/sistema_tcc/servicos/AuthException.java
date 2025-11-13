package sistema_tcc.servicos;

/**
 * Exceção customizada para falhas de login (RF001).
 * A GUI (Controlador) irá capturar isso.
 */
public class AuthException extends Exception {
    public AuthException(String message) {
        super(message);
    }
}