package sistema_tcc.ui.controlador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import sistema_tcc.dominio.Professor;
import sistema_tcc.dominio.TCC;
import sistema_tcc.servicos.SessaoUsuario;
import sistema_tcc.servicos.TccServico;
import sistema_tcc.ui.Navegacao;

import java.util.List;
import java.util.stream.Collectors;

/**
 * CORRIGIDO: A lógica de busca do usuário foi movida do construtor
 * para o método initialize(), que é chamado pelo JavaFX
 * após a tela ser carregada e o usuário estar logado.
 */
public class ProfessorControlador {

    // Serviços (Injetados)
    private final TccServico tccServico;
    private final Navegacao navegacao;
    private final SessaoUsuario sessao;

    // Atributos do FXML
    @FXML private Label lblNomeProfessor;
    @FXML private Label lblStatusGlobalProfessor;
    @FXML private ListView<String> listaTemasPropostos; // Mostra TCCs
    @FXML private Button btnEscolherTema;
    @FXML private Button btnProfLogout;

    // Estado interno
    private Professor profLogado;
    private ObservableList<String> temasObservaveis = FXCollections.observableArrayList();
    private List<TCC> tccsPropostos; // Mapeia a lista de Strings para os objetos TCC

    /**
     * Construtor para Injeção de Dependência.
     */
    public ProfessorControlador(TccServico tccServico, Navegacao navegacao) {
        this.tccServico = tccServico;
        this.navegacao = navegacao;
        this.sessao = SessaoUsuario.getInstancia();
        // REMOVIDO: Não buscar o usuário aqui, é muito cedo!
    }

    /**
     * Chamado pelo JavaFX após a tela ser carregada.
     */
    @FXML
    public void initialize() {
        try {
            // AGORA é seguro buscar o usuário
            // Esta linha chama o novo método genérico em SessaoUsuario
            this.profLogado = sessao.getUsuarioLogado(Professor.class);
            lblNomeProfessor.setText("Bem-vindo(a), Prof. " + profLogado.getNome());
            lblStatusGlobalProfessor.setText("Pronto para gerenciar.");

            carregarTemasPropostos();

        } catch (IllegalStateException e) {
            // Se algo der errado (ex: usuário não logado), mostrar erro e voltar ao login
            exibirAlerta("Erro de Sessão", e.getMessage());
            navegacao.navegarPara(Navegacao.Tela.LOGIN);
        }
    }

    /**
     * Carrega a lista de TCCs com status PROPOSTA (RF004)
     */
    private void carregarTemasPropostos() {
        // Limpa antes de carregar
        temasObservaveis.clear();

        // Busca do serviço
        this.tccsPropostos = tccServico.listarTemasPropostos();

        if (tccsPropostos.isEmpty()) {
            temasObservaveis.add("Nenhum tema proposto encontrado.");
            btnEscolherTema.setDisable(true);
        } else {
            // Converte a lista de TCC (objeto) para uma lista de String (para exibir)
            List<String> titulos = tccsPropostos.stream()
                    .map(tcc -> tcc.getTitulo() + " (Aluno: " + tcc.getAutor().getNome() + ")")
                    .collect(Collectors.toList());
            temasObservaveis.addAll(titulos);
            btnEscolherTema.setDisable(false);
        }

        listaTemasPropostos.setItems(temasObservaveis);
    }

    @FXML
    void escolherTema() {
        int indexSelecionado = listaTemasPropostos.getSelectionModel().getSelectedIndex();

        if (indexSelecionado < 0 || tccsPropostos.isEmpty()) {
            exibirAlerta("Erro", "Por favor, selecione um tema da lista.");
            return;
        }

        try {
            // Pega o objeto TCC real da nossa lista interna (não da String)
            TCC tccSelecionado = tccsPropostos.get(indexSelecionado);

            // Chama o serviço (RF005)
            tccServico.escolherOrientador(profLogado, tccSelecionado);

            exibirAlerta("Sucesso", "Você agora é o orientador de: " + tccSelecionado.getTitulo());

            // Atualiza a lista, pois o tema escolhido não está mais "proposto"
            carregarTemasPropostos();

        } catch (Exception e) {
            exibirAlerta("Erro ao escolher tema", e.getMessage());
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