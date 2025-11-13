package sistema_tcc.ui.controlador;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import sistema_tcc.dominio.Aluno;
import sistema_tcc.dominio.AreaConhecimento; // <-- IMPORT ADICIONADO
import sistema_tcc.servicos.SessaoUsuario;
import sistema_tcc.servicos.TccServico;
import sistema_tcc.ui.Navegacao;

import java.util.Collections; // <-- IMPORT JÁ EXISTENTE

/**
 * Controlador para a tela do Aluno (AlunoVista.fxml)
 */
public class AlunoControlador {

    // Serviços (Injetados)
    private final TccServico tccServico;
    private final Navegacao navegacao;
    private final SessaoUsuario sessao;

    // Atributos do FXML
    @FXML private Label lblNomeAluno;
    @FXML private Label lblStatusGlobalAluno;
    @FXML private TextField txtTemaTitulo;
    @FXML private TextArea txtTemaDescricao;
    @FXML private Button btnCadastrarTema;
    @FXML private Button btnAlunoLogout;

    // Estado interno
    private Aluno alunoLogado;

    /**
     * Construtor para Injeção de Dependência.
     */
    public AlunoControlador(TccServico tccServico, Navegacao navegacao) {
        this.tccServico = tccServico;
        this.navegacao = navegacao;
        this.sessao = SessaoUsuario.getInstancia();
    }

    /**
     * Chamado pelo JavaFX após a tela ser carregada.
     */
    @FXML
    public void initialize() {
        try {
            this.alunoLogado = sessao.getUsuarioLogado(Aluno.class);
            lblNomeAluno.setText("Bem-vindo(a), " + alunoLogado.getNome());
            lblStatusGlobalAluno.setText("Você está logado como Aluno.");
        } catch (IllegalStateException e) {
            exibirAlerta("Erro de Sessão", e.getMessage());
            navegacao.navegarPara(Navegacao.Tela.LOGIN);
        }
    }

    @FXML
    void cadastrarTema() {
        // Lógica do Caso de Uso 1 (RF002)
        String titulo = txtTemaTitulo.getText();
        String descricao = txtTemaDescricao.getText();

        if (titulo.isBlank() || descricao.isBlank()) {
            exibirAlerta("Erro de Validação", "Título e Descrição não podem estar vazios.");
            return;
        }

        try {
            // Chama o serviço desacoplado
            // CORREÇÃO: Usamos <AreaConhecimento> para dar a "dica" de tipo.
            tccServico.cadastrarTema(alunoLogado, titulo, descricao, Collections.<AreaConhecimento>emptyList());

            // Confirmação (RF003)
            exibirAlerta("Sucesso", "Seu tema '" + titulo + "' foi cadastrado e enviado para avaliação!");

            // Limpa os campos
            txtTemaTitulo.clear();
            txtTemaDescricao.clear();
            lblStatusGlobalAluno.setText("Proposta de tema enviada.");

        } catch (Exception e) {
            exibirAlerta("Erro ao Cadastrar", e.getMessage());
        }
    }

    @FXML
    void fazerLogout() {
        sessao.logout();
        navegacao.navegarPara(Navegacao.Tela.LOGIN);
    }

    private void exibirAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}