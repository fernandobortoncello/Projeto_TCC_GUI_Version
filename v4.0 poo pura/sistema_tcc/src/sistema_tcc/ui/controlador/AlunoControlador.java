package sistema_tcc.ui.controlador;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.TextFlow;
import javafx.scene.text.Text;
import sistema_tcc.sistema.sessao.SessaoAluno;
import sistema_tcc.ui.Navegacao;
import sistema_tcc.view.TccSnapshot;

public class AlunoControlador {
    private final Navegacao nav;
    private SessaoAluno sessao;

    @FXML private Label lblNomeAluno, lblTccStatusGeral, lblOrientadorNome, lblDataBanca, lblNotaFinal, lblStatusCadastro;
    @FXML private TabPane tabPaneAluno;
    @FXML private Tab tabCadastrarTema, tabOrientacoes, tabBanca;
    @FXML private TextField txtTituloTCC;
    @FXML private TextArea txtDescricaoTCC, txtAnotacoesFinais;
    @FXML private ListView<String> listaMembrosBanca;
    @FXML private ListView<Object> listaOrientacoes;
    @FXML private TextFlow textFlowStatusAta;

    public AlunoControlador(Navegacao n) { nav = n; }

    @FXML public void initialize() {
        try {
            // O controlador converte a sessão genérica para a específica
            this.sessao = (SessaoAluno) nav.lerSessao();
            lblNomeAluno.setText(sessao.lerNomeUsuario());
            atualizar();
        } catch(Exception e) { nav.irPara(Navegacao.Tela.LOGIN); }
    }

    private void atualizar() {
        TccSnapshot tcc = sessao.visualizarMeuTrabalho();
        if (tcc == null) {
            tabCadastrarTema.setDisable(false);
            tabOrientacoes.setDisable(true);
            tabBanca.setDisable(true);
            lblTccStatusGeral.setText("Sem TCC.");
        } else {
            tabCadastrarTema.setDisable(true);
            tabOrientacoes.setDisable(false);
            tabBanca.setDisable(false);

            lblTccStatusGeral.setText(tcc.status);
            lblOrientadorNome.setText(tcc.orientador);
            listaOrientacoes.setItems(FXCollections.observableArrayList(tcc.orientacoes));

            lblDataBanca.setText(tcc.bancaInfo);
            listaMembrosBanca.setItems(FXCollections.observableArrayList(tcc.membrosBanca));

            lblNotaFinal.setText(tcc.nota);
            txtAnotacoesFinais.setText(tcc.correcoes);
            textFlowStatusAta.getChildren().setAll(new Text(tcc.veredito));
        }
    }

    @FXML public void cadastrarTema() {
        try {
            sessao.submeterProposta(txtTituloTCC.getText(), txtDescricaoTCC.getText());
            lblStatusCadastro.setText("Sucesso!");
            atualizar();
        } catch(Exception e) { lblStatusCadastro.setText(e.getMessage()); }
    }

    @FXML public void fazerLogout() { nav.definirSessao(null); nav.irPara(Navegacao.Tela.LOGIN); }
}