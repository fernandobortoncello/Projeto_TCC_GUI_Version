package sistema_tcc.ui.controlador;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sistema_tcc.dominio.Papel;
import sistema_tcc.servicos.AuthException;
import sistema_tcc.servicos.AuthServico;
import sistema_tcc.servicos.dto.UsuarioLogadoDTO;
import sistema_tcc.ui.Navegacao;

/**
 * Controlador para a tela LoginVista.fxml.
 * (CORREÇÃO DE ERRO 4) Agora usa UsuarioLogadoDTO.
 */
public class LoginControlador {

    // --- Injeção de Dependência (via Construtor) ---
    private final AuthServico authServico;
    private final Navegacao navegacao;

    // --- Componentes da UI (Injetados pelo @FXML) ---
    @FXML private TextField txtLogin;
    @FXML private PasswordField txtSenha;
    @FXML private Label lblStatus;

    /**
     * Construtor usado pelo AppJavaFX para injetar dependências.
     */
    public LoginControlador(AuthServico authServico, Navegacao navegacao) {
        this.authServico = authServico;
        this.navegacao = navegacao;
    }

    @FXML
    public void initialize() {
        lblStatus.setText("Por favor, faça o login.");
    }

    /**
     * Chamado pelo clique do botão "Login" no FXML.
     */
    @FXML
    public void btnLoginClick() { // DEVE SER PÚBLICO
        String login = txtLogin.getText();
        String senha = txtSenha.getText();

        if (login.isBlank() || senha.isBlank()) {
            lblStatus.setText("Erro: Preencha todos os campos.");
            return;
        }

        try {
            // (CORREÇÃO DE ERRO 4)
            // 1. Chama o Serviço, que agora retorna um DTO
            UsuarioLogadoDTO usuarioLogado = authServico.login(login, senha);
            lblStatus.setText("Login bem-sucedido!");

            // 2. Decide para qual tela navegar com base no PAPEL do DTO
            if (usuarioLogado.getPapel() == Papel.ALUNO) {
                System.out.println("LOG: Login Aluno OK: " + usuarioLogado.getNome());
                navegacao.navegarPara(Navegacao.Tela.ALUNO_DASHBOARD);
            } else if (usuarioLogado.getPapel() == Papel.PROFESSOR || usuarioLogado.getPapel() == Papel.COORDENADOR) {
                System.out.println("LOG: Login Professor OK: " + usuarioLogado.getNome());
                navegacao.navegarPara(Navegacao.Tela.PROFESSOR_DASHBOARD);
            }

        } catch (AuthException e) {
            // 3. Exibe o erro retornado pelo serviço
            lblStatus.setText("Erro: " + e.getMessage());
        }
    }
}