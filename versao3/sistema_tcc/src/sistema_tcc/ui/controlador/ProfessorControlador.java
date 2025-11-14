package sistema_tcc.ui.controlador;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import sistema_tcc.dominio.Papel;
import sistema_tcc.servicos.SessaoUsuario;
import sistema_tcc.servicos.TccServico;
import sistema_tcc.servicos.dto.ProfessorDTO;
import sistema_tcc.servicos.dto.TccResumoDTO;
import sistema_tcc.servicos.dto.UsuarioLogadoDTO;
import sistema_tcc.ui.Navegacao;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador para a tela ProfessorVista.fxml (UC2, UC3, UC4, UC5).
 * Refatorado para usar APENAS DTOs.
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
    @FXML private ListView<TccResumoDTO> listaTemasPropostos;
    @FXML private Label lblStatusUC2;

    // --- Componentes UI (Aba UC3: Registrar Orientação) ---
    @FXML private ComboBox<TccResumoDTO> comboTccsOrientandos;
    @FXML private DatePicker datePickerOrientacao;
    @FXML private TextArea textAreaDescricao;
    @FXML private Label lblStatusUC3;

    // --- Componentes UI (Aba UC4: Definir Banca) ---
    @FXML private ComboBox<TccResumoDTO> comboTccsParaBanca;
    @FXML private ListView<ProfessorDTO> listaProfessoresDisponiveis;
    @FXML private DatePicker datePickerBanca;
    @FXML private Label lblStatusUC4;

    // --- Componentes UI (Aba UC5: Finalizar TCC) ---
    @FXML private ComboBox<TccResumoDTO> comboTccsParaFinalizar;
    @FXML private TextField txtNotaFinal;
    @FXML private TextArea txtAnotacoesFinais;
    @FXML private Label lblStatusUC5;

    private UsuarioLogadoDTO profLogado;

    public ProfessorControlador(TccServico tccServico, Navegacao navegacao, SessaoUsuario sessaoUsuario) {
        this.tccServico = tccServico;
        this.navegacao = navegacao;
        this.sessaoUsuario = sessaoUsuario;
    }

    @FXML
    public void initialize() {
        try {
            // Tenta obter o DTO como Professor ou Coordenador
            this.profLogado = sessaoUsuario.getUsuarioLogado();
            if (profLogado.getPapel() == Papel.PROFESSOR) {
                lblNomeProfessor.setText(profLogado.getNome());
            } else if (profLogado.getPapel() == Papel.COORDENADOR) {
                lblNomeProfessor.setText(profLogado.getNome() + " (Coordenador)");
            } else {
                throw new IllegalStateException("Usuário não é Professor ou Coordenador.");
            }

            listaProfessoresDisponiveis.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            carregarDadosTodasAbas();

        } catch (IllegalStateException e) {
            lblNomeProfessor.setText("ERRO DE SESSÃO");
            navegacao.navegarPara(Navegacao.Tela.LOGIN);
        }
    }

    private void carregarDadosTodasAbas() {
        carregarAbaEscolherTema();
        carregarAbaRegistrarOrientacao();
        carregarAbaDefinirBanca();
        carregarAbaFinalizarTCC();
    }

    // --- Lógica da Aba UC2: Escolher Tema ---
    private void carregarAbaEscolherTema() {
        try {
            List<TccResumoDTO> temas = tccServico.listarTemasPropostos(); // RF004
            listaTemasPropostos.setItems(FXCollections.observableArrayList(temas));
            lblStatusUC2.setText(temas.size() + " temas aguardando orientador.");
        } catch (Exception e) {
            lblStatusUC2.setText("Erro ao carregar temas: " + e.getMessage());
        }
    }

    @FXML
    private void escolherOrientador() {
        TccResumoDTO tccSelecionado = listaTemasPropostos.getSelectionModel().getSelectedItem();
        if (tccSelecionado == null) {
            lblStatusUC2.setText("Erro: Nenhum TCC selecionado.");
            return;
        }

        try {
            tccServico.escolherOrientador(profLogado.getId(), tccSelecionado.getId()); // RF005
            lblStatusUC2.setText("TCC '" + tccSelecionado + "' assumido com sucesso!");
            carregarDadosTodasAbas();

        } catch (Exception e) {
            lblStatusUC2.setText("Erro: " + e.getMessage());
        }
    }

    // --- Lógica da Aba UC3: Registrar Orientação ---
    private void carregarAbaRegistrarOrientacao() {
        try {
            List<TccResumoDTO> temas = tccServico.listarTCCsOrientados(profLogado.getId());
            comboTccsOrientandos.setItems(FXCollections.observableArrayList(temas));
            lblStatusUC3.setText("");
        } catch (Exception e) {
            lblStatusUC3.setText("Erro ao carregar TCCs: " + e.getMessage());
        }
    }

    @FXML
    private void salvarOrientacao() {
        TccResumoDTO tccSelecionado = comboTccsOrientandos.getValue();
        LocalDate data = datePickerOrientacao.getValue();
        String descricao = textAreaDescricao.getText();

        if (tccSelecionado == null || data == null || descricao.isBlank()) {
            lblStatusUC3.setText("Erro: Preencha TCC, Data e Descrição.");
            return;
        }

        try {
            tccServico.registrarOrientacao(profLogado.getId(), tccSelecionado.getId(), data, descricao); // RF009
            lblStatusUC3.setText("Orientação salva com sucesso! (RF010)");

            datePickerOrientacao.setValue(null);
            textAreaDescricao.clear();

            carregarDadosTodasAbas();

        } catch (Exception e) {
            lblStatusUC3.setText("Erro ao salvar: " + e.getMessage());
        }
    }

    // --- Lógica da Aba UC4: Definir Banca ---
    private void carregarAbaDefinirBanca() {
        try {
            List<ProfessorDTO> professores = tccServico.listarProfessores();
            listaProfessoresDisponiveis.setItems(FXCollections.observableArrayList(professores));

            List<TccResumoDTO> tccs = tccServico.listarTccsParaDefinirBanca(profLogado.getId());
            comboTccsParaBanca.setItems(FXCollections.observableArrayList(tccs));
            lblStatusUC4.setText(tccs.size() + " TCCs prontos para banca.");

        } catch (Exception e) {
            lblStatusUC4.setText("Erro ao carregar dados: " + e.getMessage());
        }
    }

    @FXML
    private void onDefinirBancaClick() {
        TccResumoDTO tcc = comboTccsParaBanca.getValue();
        List<ProfessorDTO> membrosDTO = listaProfessoresDisponiveis.getSelectionModel().getSelectedItems();
        LocalDate data = datePickerBanca.getValue();

        if (tcc == null || membrosDTO.isEmpty() || data == null) {
            lblStatusUC4.setText("Erro: Selecione TCC, Data e Membros.");
            return;
        }

        try {
            // Mapeia DTOs de volta para IDs
            List<String> idsMembros = membrosDTO.stream()
                    .map(ProfessorDTO::getId)
                    .collect(Collectors.toList());

            tccServico.definirBanca(tcc.getId(), idsMembros, data); // RF012, RF013
            lblStatusUC4.setText("Banca definida com sucesso! (RF015)");
            carregarDadosTodasAbas();
        } catch (Exception e) {
            lblStatusUC4.setText("Erro: " + e.getMessage());
        }
    }

    // --- Lógica da Aba UC5: Finalizar TCC ---
    private void carregarAbaFinalizarTCC() {
        try {
            List<TccResumoDTO> tccs = tccServico.listarTccsParaFinalizar(profLogado.getId());
            comboTccsParaFinalizar.setItems(FXCollections.observableArrayList(tccs));
            lblStatusUC5.setText(tccs.size() + " TCCs aguardando finalização.");
        } catch (Exception e) {
            lblStatusUC5.setText("Erro ao carregar dados: " + e.getMessage());
        }
    }

    @FXML
    private void onFinalizarTccClick() {
        TccResumoDTO tcc = comboTccsParaFinalizar.getValue();
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
            tccServico.finalizarTCC(tcc.getId(), nota, anotacoes); // RF017, RF019
            lblStatusUC5.setText("TCC finalizado com sucesso!");

            // Simula RF020 (Gerar Ata)
            gerarTextoAta(tcc.getId());

            carregarDadosTodasAbas();

        } catch (Exception e) {
            lblStatusUC5.setText("Erro: " + e.getMessage());
        }
    }

    /**
     * Simula a geração da ATA (RF020)
     */
    private void gerarTextoAta(String tccId) {
        // NOTA: Esta lógica viola o "Information Expert" para o controlador,
        // mas é aceitável para um pop-up simples.
        // O ideal seria o TccServico ter um método getAtaDTO(tccId)

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("ATA Gerada (RF020)");
        alert.setHeaderText("Ata de Defesa do TCC (Simulação)");
        alert.setContentText("A ATA para o TCC ID: " + tccId + " foi gerada e enviada.");
        alert.showAndWait();
    }

    @FXML
    public void fazerLogout() { // DEVE SER PÚBLICO
        sessaoUsuario.logout();
        navegacao.navegarPara(Navegacao.Tela.LOGIN);
    }
}