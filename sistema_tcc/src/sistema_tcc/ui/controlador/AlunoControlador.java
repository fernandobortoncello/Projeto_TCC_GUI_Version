package sistema_tcc.ui.controlador;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import sistema_tcc.dominio.*;
import sistema_tcc.servicos.SessaoUsuario;
import sistema_tcc.servicos.TccServico;
import sistema_tcc.ui.Navegacao;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Controlador para a tela AlunoVista.fxml (UC1, UC3, UC5).
 */
public class AlunoControlador {

    // --- Dependências ---
    private final TccServico tccServico;
    private final Navegacao navegacao;
    private final SessaoUsuario sessaoUsuario;

    // --- Componentes UI (Geral) ---
    @FXML private Label lblNomeAluno;
    @FXML private TabPane tabPaneAluno;

    // --- Componentes UI (Aba UC1: Cadastrar Tema) ---
    @FXML private TextField txtTituloTCC;
    @FXML private TextArea txtDescricaoTCC;
    @FXML private Label lblStatusCadastro;
    @FXML private Tab tabCadastrarTema;

    // --- Componentes UI (Aba UC3: Minhas Orientações) ---
    @FXML private Tab tabOrientacoes;
    @FXML private ListView<Orientacao> listaOrientacoes;

    // --- Componentes UI (Aba UC5: Minha Banca / Nota) ---
    @FXML private Tab tabBanca;
    @FXML private Label lblTccStatusGeral;
    @FXML private Label lblOrientadorNome;
    @FXML private Label lblDataBanca;
    @FXML private ListView<Professor> listaMembrosBanca;
    @FXML private Label lblNotaFinal;
    @FXML private TextArea txtAnotacoesFinais;
    @FXML private TextFlow textFlowStatusAta;


    private Aluno alunoLogado;
    private TCC meuTCC;

    public AlunoControlador(TccServico tccServico, Navegacao navegacao, SessaoUsuario sessaoUsuario) {
        this.tccServico = tccServico;
        this.navegacao = navegacao;
        this.sessaoUsuario = sessaoUsuario;
    }

    /**
     * Chamado quando a tela é carregada.
     */
    @FXML
    public void initialize() {
        try {
            this.alunoLogado = sessaoUsuario.getUsuarioLogado(Aluno.class);
            lblNomeAluno.setText(alunoLogado.getNome());

            // Carrega o TCC do aluno
            carregarDadosTCC();

        } catch (IllegalStateException e) {
            lblNomeAluno.setText("ERRO DE SESSÃO");
            navegacao.navegarPara(Navegacao.Tela.LOGIN);
        }
    }

    /**
     * Carrega todos os dados relacionados ao TCC do aluno e atualiza a UI.
     */
    private void carregarDadosTCC() {
        this.meuTCC = tccServico.buscarTccPorAluno(alunoLogado);

        if (meuTCC == null) {
            // Aluno ainda não cadastrou tema
            tabCadastrarTema.setDisable(false);
            tabOrientacoes.setDisable(true);
            tabBanca.setDisable(true);
            lblTccStatusGeral.setText("Nenhum TCC cadastrado.");
        } else {
            // Aluno já cadastrou tema
            tabCadastrarTema.setDisable(true); // Desabilita o cadastro
            tabOrientacoes.setDisable(false);
            tabBanca.setDisable(false);

            lblTccStatusGeral.setText("Status Atual: " + meuTCC.getStatus().toString());

            // Aba Orientações (UC3)
            listaOrientacoes.setItems(FXCollections.observableArrayList(meuTCC.getOrientacoes()));

            // Aba Banca e Nota (UC4 e UC5)
            if (meuTCC.getOrientador() != null) {
                lblOrientadorNome.setText("Orientador(a): " + meuTCC.getOrientador().getNome());
            } else {
                lblOrientadorNome.setText("Orientador(a): (Aguardando definição)");
            }

            if (meuTCC.getBanca() != null) {
                lblDataBanca.setText("Data da Banca: " + meuTCC.getBanca().getDataApresentacaoFormatada());
                listaMembrosBanca.setItems(FXCollections.observableArrayList(meuTCC.getBanca().getMembros()));
            } else {
                lblDataBanca.setText("Data da Banca: (Aguardando definição)");
            }

            if (meuTCC.getAvaliacaoFinal() != null) {
                Avaliacao avaliacao = meuTCC.getAvaliacaoFinal();
                lblNotaFinal.setText("Nota Final: " + avaliacao.getNotaFinal());
                txtAnotacoesFinais.setText(avaliacao.getAnotacoes());

                // Lógica da ATA (RF020)
                Text statusText = new Text(avaliacao.getStatus());
                if(avaliacao.getStatus().equals("APROVADO")) {
                    statusText.setStyle("-fx-fill: green; -fx-font-weight: bold;");
                } else {
                    statusText.setStyle("-fx-fill: red; -fx-font-weight: bold;");
                }
                textFlowStatusAta.getChildren().setAll(new Text("Resultado: "), statusText);
            } else {
                lblNotaFinal.setText("Nota Final: (Aguardando avaliação)");
                txtAnotacoesFinais.setText("");
                textFlowStatusAta.getChildren().clear();
            }
        }
    }

    /**
     * Chamado pelo clique do botão "Cadastrar Tema".
     */
    @FXML
    private void cadastrarTema() {
        String titulo = txtTituloTCC.getText();
        String descricao = txtDescricaoTCC.getText();

        if (titulo.isBlank() || descricao.isBlank()) {
            lblStatusCadastro.setText("Erro: Título e Descrição são obrigatórios.");
            return;
        }

        try {
            tccServico.cadastrarTema(
                    alunoLogado,
                    titulo,
                    descricao,
                    // TODO: Adicionar um ComboBox ou CheckBox para Áreas
                    Collections.singletonList(AreaConhecimento.ENGENHARIA_SOFTWARE) // Simulado
            );

            lblStatusCadastro.setText("Tema cadastrado com sucesso!");

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sucesso");
            alert.setHeaderText("Cadastro Realizado (RF003)");
            alert.setContentText("Seu tema foi enviado para análise dos professores.");
            alert.showAndWait();

            // Recarrega todos os dados e atualiza as abas
            carregarDadosTCC();
            tabPaneAluno.getSelectionModel().select(tabOrientacoes);

        } catch (Exception e) {
            lblStatusCadastro.setText("Erro ao cadastrar: " + e.getMessage());
        }
    }

    /**
     * Chamado pelo clique do botão "Logout".
     * MUDANÇA: de 'private' para 'public'
     */
    @FXML
    public void fazerLogout() {
        sessaoUsuario.logout();
        navegacao.navegarPara(Navegacao.Tela.LOGIN);
    }
}