package sistema_tcc.ui.controlador;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import sistema_tcc.sistema.Sistema;
import sistema_tcc.sistema.sessao.*;
import sistema_tcc.ui.Navegacao;

public class LoginControlador {
    private final Sistema sistema;
    private final Navegacao nav;
    @FXML private TextField txtLogin;
    @FXML private PasswordField txtSenha;
    @FXML private Label lblStatus;

    public LoginControlador(Sistema s, Navegacao n) { sistema = s; nav = n; }

    @FXML public void btnLoginClick() {
        try {
            // Polimorfismo em ação: Recebemos uma Sessão genérica
            Sessao sessao = sistema.solicitarAcesso(txtLogin.getText(), txtSenha.getText());
            nav.definirSessao(sessao);

            // Decisão baseada no tipo da Sessão (não no dado do usuário)
            if (sessao instanceof SessaoAluno) nav.irPara(Navegacao.Tela.ALUNO);
            else if (sessao instanceof SessaoProfessor) nav.irPara(Navegacao.Tela.PROFESSOR);

        } catch (Exception e) { lblStatus.setText(e.getMessage()); }
    }
}