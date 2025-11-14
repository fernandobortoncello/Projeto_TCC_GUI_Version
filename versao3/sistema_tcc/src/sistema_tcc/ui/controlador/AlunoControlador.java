package sistema_tcc.ui.controlador;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import sistema_tcc.dominio.Papel;
import sistema_tcc.servicos.SessaoUsuario;
import sistema_tcc.servicos.TccServico;
import sistema_tcc.servicos.dto.*;
import sistema_tcc.ui.Navegacao;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador para a tela AlunoVista.fxml (UC1, UC3, UC5).
 * Refatorado para usar APENAS DTOs.
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
    @FXML private ListView<OrientacaoDTO> listaOrientacoes;

    // --- Componentes UI (Aba UC5: Minha Banca / Nota) ---
    @FXML private Tab tabBanca;
    @FXML private Label lblTccStatusGeral;
    @FXML private Label lblOrientadorNome;
    @FXML private Label lblDataBanca;
    @FXML private ListView<String> listaMembrosBanca; // Alterado para String
    @FXML private Label lblNotaFinal;
    @FXML private TextArea txtAnotacoesFinais;
    @FXML private TextFlow textFlowStatusAta;

    private UsuarioLogadoDTO alunoLogado;

    public AlunoControlador(TccServico tccServico, Navegacao navegacao, SessaoUsuario sessaoUsuario) {
        this.tccServico = tccServico;
        this.navegacao = navegacao;
        this.sessaoUsuario = sessaoUsuario;
    }

    @FXML
    public void initialize() {
        try {
            // Obtém o DTO da sessão
            this.alunoLogado = sessaoUsuario.getUsuarioLogado(Papel.ALUNO);
            lblNomeAluno.setText(alunoLogado.getNome());
            carregarDadosTCC();
        } catch (IllegalStateException e) {
            lblNomeAluno.setText("ERRO DE SESSÃO");
            navegacao.navegarPara(Navegacao.Tela.LOGIN);
        }
    }

    private void carregarDadosTCC() {
        // O serviço agora retorna um DTO, não um objeto de domínio
        TccDTO meuTCC = tccServico.buscarTccDtoPorAluno(alunoLogado.getId());

        if (meuTCC == null) {
            tabCadastrarTema.setDisable(false);
            tabOrientacoes.setDisable(true);
            tabBanca.setDisable(true);
            lblTccStatusGeral.setText("Nenhum TCC cadastrado.");
        } else {
            tabCadastrarTema.setDisable(true);
            tabOrientacoes.setDisable(false);
            tabBanca.setDisable(false);

            lblTccStatusGeral.setText("Status Atual: " + meuTCC.getStatus());
            lblOrientadorNome.setText("Orientador(a): " + meuTCC.getOrientadorNome());

            // Aba Orientações (UC3)
            listaOrientacoes.setItems(FXCollections.observableArrayList(meuTCC.getOrientacoes()));

            // Aba Banca e Nota (UC4 e UC5)
            BancaDTO banca = meuTCC.getBanca();
            if (banca != null) {
                lblDataBanca.setText("Data da Banca: " + banca.getDataApresentacao());
                // Mapeia DTOs de Professor para Strings de Nome
                List<String> nomesMembros = banca.getMembros().stream()
                        .map(ProfessorDTO::getNome)
                        .collect(Collectors.toList());
                listaMembrosBanca.setItems(FXCollections.observableArrayList(nomesMembros));
            } else {
                lblDataBanca.setText("Data da Banca: (Aguardando definição)");
                listaMembrosBanca.getItems().clear();
            }

            AvaliacaoDTO avaliacao = meuTCC.getAvaliacaoFinal();
            if (avaliacao != null) {
                lblNotaFinal.setText("Nota Final: " + avaliacao.getNotaFinal());
                txtAnotacoesFinais.setText(avaliacao.getAnotacoes());

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

    @FXML
    private void cadastrarTema() {
        String titulo = txtTituloTCC.getText();
        String descricao = txtDescricaoTCC.getText();

        if (titulo.isBlank() || descricao.isBlank()) {
            lblStatusCadastro.setText("Erro: Título e Descrição são obrigatórios.");
            return;
        }

        try {
            // Passa o ID (String) do aluno
            tccServico.cadastrarTema(alunoLogado.getId(), titulo, descricao);

            lblStatusCadastro.setText("Tema cadastrado com sucesso!");

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sucesso");
            alert.setHeaderText("Cadastro Realizado (RF003)");
            alert.setContentText("Seu tema foi enviado para análise dos professores.");
            alert.showAndWait();

            carregarDadosTCC();
            tabPaneAluno.getSelectionModel().select(tabOrientacoes);

        } catch (Exception e) {
            lblStatusCadastro.setText("Erro ao cadastrar: " + e.getMessage());
        }
    }

    @FXML
    public void fazerLogout() {
        sessaoUsuario.logout();
        navegacao.navegarPara(Navegacao.Tela.LOGIN);
    }
}