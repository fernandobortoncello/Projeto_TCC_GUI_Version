package sistema_tcc.ui.controlador;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import sistema_tcc.dominio.*;
import sistema_tcc.servicos.SessaoUsuario;
import sistema_tcc.servicos.TccServico;
import sistema_tcc.ui.Navegacao;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador para a tela ProfessorVista.fxml (UC2, UC3, UC4, UC5).
 */
public class ProfessorControlador {

    // --- Dependências ---
    private final TccServico tccServico;
    private final Navegacao navegacao;
    private final SessaoUsuario sessaoUsuario;

    // --- Componentes UI (Geral) ---
    @FXML private Label lblNomeProfessor;
    @FXML private TabPane tabPaneProfessor;

    // --- Componentes UI (Aba UC2: Escolher Tema) ---
    @FXML private ListView<TCC> listaTemasPropostos;
    @FXML private Label lblStatusUC2;

    // --- Componentes UI (Aba UC3: Registrar Orientação) ---
    @FXML private ComboBox<TCC> comboTccsOrientandos;
    @FXML private DatePicker datePickerOrientacao;
    @FXML private TextArea textAreaDescricao;
    @FXML private Label lblStatusUC3;

    // --- Componentes UI (Aba UC4: Definir Banca) ---
    @FXML private ComboBox<TCC> comboTccsParaBanca;
    @FXML private ListView<Professor> listaProfessoresDisponiveis;
    @FXML private DatePicker datePickerBanca;
    @FXML private Label lblStatusUC4;

    // --- Componentes UI (Aba UC5: Finalizar TCC) ---
    @FXML private ComboBox<TCC> comboTccsParaFinalizar;
    @FXML private TextField txtNotaFinal;
    @FXML private TextArea txtAnotacoesFinais;
    @FXML private Label lblStatusUC5;

    private Professor profLogado;

    public ProfessorControlador(TccServico tccServico, Navegacao navegacao, SessaoUsuario sessaoUsuario) {
        this.tccServico = tccServico;
        this.navegacao = navegacao;
        this.sessaoUsuario = sessaoUsuario;
    }

    @FXML
    public void initialize() {
        try {
            this.profLogado = sessaoUsuario.getUsuarioLogado(Professor.class);
            lblNomeProfessor.setText(profLogado.getNome());

            // Configura a lista de professores para seleção múltipla
            listaProfessoresDisponiveis.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

            // Carrega os dados para todas as abas
            carregarDadosTodasAbas();

        } catch (IllegalStateException e) {
            lblNomeProfessor.setText("ERRO DE SESSÃO");
            navegacao.navegarPara(Navegacao.Tela.LOGIN);
        }
    }

    /**
     * Atualiza os dados de todas as abas do professor.
     * Chamado no início e após cada ação importante.
     */
    private void carregarDadosTodasAbas() {
        carregarAbaEscolherTema();
        carregarAbaRegistrarOrientacao();
        carregarAbaDefinirBanca();
        carregarAbaFinalizarTCC();
    }

    // --- Lógica da Aba UC2: Escolher Tema ---
    private void carregarAbaEscolherTema() {
        try {
            List<TCC> temas = tccServico.listarTemasPropostos(); // RF004
            listaTemasPropostos.setItems(FXCollections.observableArrayList(temas));
            lblStatusUC2.setText(temas.size() + " temas aguardando orientador.");
        } catch (Exception e) {
            lblStatusUC2.setText("Erro ao carregar temas: " + e.getMessage());
        }
    }

    @FXML
    private void escolherOrientador() {
        TCC tccSelecionado = listaTemasPropostos.getSelectionModel().getSelectedItem();
        if (tccSelecionado == null) {
            lblStatusUC2.setText("Erro: Nenhum TCC selecionado.");
            return;
        }

        try {
            tccServico.escolherOrientador(profLogado, tccSelecionado); // RF005
            lblStatusUC2.setText("TCC '" + tccSelecionado.getTitulo() + "' assumido com sucesso!");
            carregarDadosTodasAbas(); // Atualiza todas as listas

        } catch (Exception e) {
            lblStatusUC2.setText("Erro: " + e.getMessage());
        }
    }

    // --- Lógica da Aba UC3: Registrar Orientação ---
    private void carregarAbaRegistrarOrientacao() {
        try {
            List<TCC> temas = tccServico.listarTCCsOrientados(profLogado);
            comboTccsOrientandos.setItems(FXCollections.observableArrayList(temas));
            lblStatusUC3.setText("");
        } catch (Exception e) {
            lblStatusUC3.setText("Erro ao carregar TCCs: " + e.getMessage());
        }
    }

    @FXML
    private void salvarOrientacao() {
        TCC tccSelecionado = comboTccsOrientandos.getValue();
        LocalDate data = datePickerOrientacao.getValue();
        String descricao = textAreaDescricao.getText();

        if (tccSelecionado == null || data == null || descricao.isBlank()) {
            lblStatusUC3.setText("Erro: Preencha TCC, Data e Descrição.");
            return;
        }

        try {
            tccServico.registrarOrientacao(profLogado, tccSelecionado, data, descricao); // RF009
            lblStatusUC3.setText("Orientação salva com sucesso! (RF010)");

            // Limpa os campos
            datePickerOrientacao.setValue(null);
            textAreaDescricao.clear();

            // Recarrega a lista (caso o status mude para "pronto para banca")
            carregarDadosTodasAbas();

        } catch (Exception e) {
            lblStatusUC3.setText("Erro ao salvar: " + e.getMessage());
        }
    }

    // --- Lógica da Aba UC4: Definir Banca ---
    private void carregarAbaDefinirBanca() {
        try {
            // RF011
            List<Professor> professores = tccServico.listarProfessores();
            listaProfessoresDisponiveis.setItems(FXCollections.observableArrayList(professores));

            List<TCC> tccs = tccServico.listarTccsParaDefinirBanca(profLogado);
            comboTccsParaBanca.setItems(FXCollections.observableArrayList(tccs));
            lblStatusUC4.setText(tccs.size() + " TCCs prontos para banca.");

        } catch (Exception e) {
            lblStatusUC4.setText("Erro ao carregar dados: " + e.getMessage());
        }
    }

    @FXML
    private void onDefinirBancaClick() {
        TCC tcc = comboTccsParaBanca.getValue();
        List<Professor> membros = listaProfessoresDisponiveis.getSelectionModel().getSelectedItems();
        LocalDate data = datePickerBanca.getValue();

        if (tcc == null || membros.isEmpty() || data == null) {
            lblStatusUC4.setText("Erro: Selecione TCC, Data e Membros.");
            return;
        }

        try {
            tccServico.definirBanca(tcc, membros, data); // RF012, RF013
            lblStatusUC4.setText("Banca definida com sucesso! (RF015)");
            carregarDadosTodasAbas(); // Atualiza todas as listas
        } catch (Exception e) {
            lblStatusUC4.setText("Erro: " + e.getMessage());
        }
    }

    // --- Lógica da Aba UC5: Finalizar TCC ---
    private void carregarAbaFinalizarTCC() {
        try {
            List<TCC> tccs = tccServico.listarTccsParaFinalizar(profLogado);
            comboTccsParaFinalizar.setItems(FXCollections.observableArrayList(tccs));
            lblStatusUC5.setText(tccs.size() + " TCCs aguardando finalização.");
        } catch (Exception e) {
            lblStatusUC5.setText("Erro ao carregar dados: " + e.getMessage());
        }
    }

    @FXML
    private void onFinalizarTccClick() {
        TCC tcc = comboTccsParaFinalizar.getValue();
        String notaStr = txtNotaFinal.getText();
        String anotacoes = txtAnotacoesFinais.getText();

        double nota;
        try {
            nota = Double.parseDouble(notaStr);
        } catch (NumberFormatException e) {
            lblStatusUC5.setText("Erro: Nota deve ser um número.");
            return;
        }

        if (tcc == null) {
            lblStatusUC5.setText("Erro: Selecione um TCC.");
            return;
        }

        try {
            tccServico.finalizarTCC(tcc, nota, anotacoes); // RF017, RF019
            lblStatusUC5.setText("TCC finalizado com sucesso!");

            // Simula RF020 (Gerar Ata)
            gerarTextoAta(tcc);

            carregarDadosTodasAbas(); // Atualiza todas as listas

        } catch (Exception e) {
            lblStatusUC5.setText("Erro: " + e.getMessage());
        }
    }

    /**
     * Simula a geração da ATA (RF020)
     */
    private void gerarTextoAta(TCC tcc) {
        Avaliacao avaliacao = tcc.getAvaliacaoFinal();
        String statusFormatado = String.format(
                "** %s **",
                avaliacao.getStatus()
        );

        String ata = String.format(
                "ATA DE DEFESA DE TCC\n" +
                        "==================================\n" +
                        "Aluno: %s\n" +
                        "Título: %s\n" +
                        "Data da Defesa: %s\n\n" +
                        "NOTA FINAL: %.2f\n" +
                        "STATUS: %s\n\n" +
                        "Anotações:\n%s\n" +
                        "==================================\n",
                tcc.getAutor().getNome(),
                tcc.getTitulo(),
                tcc.getBanca().getDataApresentacaoFormatada(),
                avaliacao.getNotaFinal(),
                statusFormatado,
                avaliacao.getAnotacoes()
        );

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("ATA Gerada (RF020)");
        alert.setHeaderText("Ata de Defesa do TCC");
        // Usar um TextArea para preservar a formatação
        TextArea textArea = new TextArea(ata);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        alert.getDialogPane().setContent(textArea);
        alert.showAndWait();
    }

    // --- Geral ---
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