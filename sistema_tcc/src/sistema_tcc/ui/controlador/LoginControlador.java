package sistema_tcc.ui.controlador;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sistema_tcc.dominio.Aluno;
import sistema_tcc.dominio.Professor;
import sistema_tcc.dominio.Usuario;
import sistema_tcc.servicos.AuthException;
import sistema_tcc.servicos.AuthServico;
import sistema_tcc.servicos.SessaoUsuario;
import sistema_tcc.ui.Navegacao;

/**
 * Controlador para a tela LoginVista.fxml.
 * Este controlador TEM o método btnLoginClick().
 */
public class LoginControlador {

    private final AuthServico authServico;
    private final Navegacao navegacao;

    @FXML private TextField txtCpf;
    @FXML private PasswordField txtSenha;
    @FXML private Label lblStatusLogin;

    // Construtor usado pela AppJavaFX (Injeção de Dependência)
    public LoginControlador(AuthServico authServico, Navegacao navegacao) {
        this.authServico = authServico;
        this.navegacao = navegacao;
    }

    @FXML
    public void initialize() {
        lblStatusLogin.setText("Por favor, insira suas credenciais.");
        lblStatusLogin.setStyle("-fx-text-fill: black;");
    }

    /**
     * Chamado quando o botão "Entrar" é clicado.
     * Este é o método que o FXML deve chamar.
     */
    @FXML
    private void btnLoginClick() {
        String cpf = txtCpf.getText();
        String senha = txtSenha.getText();

        if (cpf.isBlank() || senha.isBlank()) {
            lblStatusLogin.setText("CPF (Matrícula) e Senha são obrigatórios.");
            lblStatusLogin.setStyle("-fx-text-fill: red;");
            return;
        }

        try {
            // 1. Chamar o Serviço de Autenticação
            Usuario usuarioLogado = authServico.login(cpf, senha);

            // 2. Definir o usuário na Sessão
            SessaoUsuario.getInstancia().setUsuarioLogado(usuarioLogado);

            // 3. Navegar para a tela correta baseado no Papel (RNF-SEG02)
            if (usuarioLogado instanceof Aluno) {
                navegacao.navegarPara(Navegacao.Tela.ALUNO_DASHBOARD);
            } else if (usuarioLogado instanceof Professor) {
                navegacao.navegarPara(Navegacao.Tela.PROFESSOR_DASHBOARD);
            }

        } catch (AuthException e) {
            // Exibe erros de login (ex: "Senha incorreta")
            lblStatusLogin.setText(e.getMessage());
            lblStatusLogin.setStyle("-fx-text-fill: red;");
            showAlert(Alert.AlertType.ERROR, "Erro de Login", e.getMessage());
        } catch (Exception e) {
            // Exibe erros inesperados
            lblStatusLogin.setText("Erro inesperado: " + e.getMessage());
            lblStatusLogin.setStyle("-fx-text-fill: red;");
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}