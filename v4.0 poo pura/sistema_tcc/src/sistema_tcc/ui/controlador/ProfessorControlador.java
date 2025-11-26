package sistema_tcc.ui.controlador;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;
import sistema_tcc.sistema.sessao.SessaoProfessor;
import sistema_tcc.ui.Navegacao;
import sistema_tcc.view.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

/**
 * Controlador V5.0 (Pure OO / Alan Kay Style).
 * Interage com a SessãoProfessor e manipula Snapshots.
 */
public class ProfessorControlador {
    private final Navegacao nav;
    private SessaoProfessor sessao;

    @FXML private Label lblNomeProfessor, lblStatusUC2, lblStatusUC3, lblStatusUC4, lblStatusUC5;
    @FXML private ListView<TccSnapshot> listaTemasPropostos;
    @FXML private ComboBox<TccSnapshot> comboTccsOrientandos, comboTccsParaBanca, comboTccsParaFinalizar;
    @FXML private DatePicker datePickerOrientacao, datePickerBanca;
    @FXML private TextArea textAreaDescricao, txtAnotacoesFinais;
    @FXML private ListView<ProfessorSnapshot> listaProfessoresDisponiveis;
    @FXML private TextField txtNotaFinal;

    public ProfessorControlador(Navegacao n) { nav = n; }

    @FXML public void initialize() {
        try {
            // Obtém a sessão específica do Professor
            this.sessao = (SessaoProfessor) nav.lerSessao();
            lblNomeProfessor.setText(sessao.lerNomeUsuario());

            listaProfessoresDisponiveis.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            atualizar();
        } catch(Exception e) { nav.irPara(Navegacao.Tela.LOGIN); }
    }

    private void atualizar() {
        listaTemasPropostos.setItems(FXCollections.observableArrayList(sessao.verPropostasDisponiveis()));
        comboTccsOrientandos.setItems(FXCollections.observableArrayList(sessao.verMeusOrientandos()));
        listaProfessoresDisponiveis.setItems(FXCollections.observableArrayList(sessao.listarColegas()));
        comboTccsParaBanca.setItems(FXCollections.observableArrayList(sessao.verTccsProntosParaBanca()));
        comboTccsParaFinalizar.setItems(FXCollections.observableArrayList(sessao.verTccsParaAvaliar()));
    }

    @FXML public void escolherOrientador() {
        try {
            TccSnapshot t = listaTemasPropostos.getSelectionModel().getSelectedItem();
            if (t != null) {
                sessao.assumirOrientacao(t.id);
                atualizar();
                lblStatusUC2.setText("OK - Orientação Assumida");
            }
        } catch(Exception e) { lblStatusUC2.setText(e.getMessage()); }
    }

    @FXML public void salvarOrientacao() {
        try {
            TccSnapshot t = comboTccsOrientandos.getValue();
            if (t != null && datePickerOrientacao.getValue() != null) {
                sessao.registrarOrientacao(t.id, datePickerOrientacao.getValue(), textAreaDescricao.getText());
                lblStatusUC3.setText("OK - Orientação Registrada");
                atualizar();
            }
        } catch(Exception e) { lblStatusUC3.setText(e.getMessage()); }
    }

    @FXML public void onDefinirBancaClick() {
        try {
            TccSnapshot t = comboTccsParaBanca.getValue();
            var membros = listaProfessoresDisponiveis.getSelectionModel().getSelectedItems();
            if (t != null && !membros.isEmpty() && datePickerBanca.getValue() != null) {
                var ids = membros.stream().map(p -> p.id).collect(Collectors.toList());
                sessao.agendarBanca(t.id, datePickerBanca.getValue(), ids);
                lblStatusUC4.setText("OK - Banca Definida");
                atualizar();
            }
        } catch(Exception e) { lblStatusUC4.setText(e.getMessage()); }
    }

    @FXML public void onFinalizarTccClick() {
        try {
            TccSnapshot t = comboTccsParaFinalizar.getValue();
            String notaTexto = txtNotaFinal.getText();
            String parecer = txtAnotacoesFinais.getText();

            if (t != null && !notaTexto.isBlank()) {
                double nota = Double.parseDouble(notaTexto);

                // 1. Envia mensagem para o sistema (Mudança de Estado)
                sessao.avaliarFinal(t.id, nota, parecer);

                lblStatusUC5.setText("OK - TCC Finalizado");
                atualizar();

                // 2. Exibe a mensagem de ATA (Nova Funcionalidade)
                exibirMensagemGeracaoAta(t, nota, parecer);
            }
        } catch(Exception e) { lblStatusUC5.setText(e.getMessage()); }
    }

    /**
     * Gera o pop-up simulando a ATA e notificações.
     * Usa os dados do Snapshot (V5.0) e os inputs locais.
     */
    private void exibirMensagemGeracaoAta(TccSnapshot tcc, double nota, String parecer) {
        String statusAprovacao = (nota >= 6.0) ? "APROVADO" : "REPROVADO";
        String dataHoje = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        // Simulação do conteúdo do e-mail/documento
        String mensagem = String.format(
                "✅ ATA DE DEFESA GERADA COM SUCESSO!\n\n" +
                        "O sistema realizou as seguintes ações:\n" +
                        "1. O status do TCC foi alterado para FINALIZADO.\n" +
                        "2. O documento 'ATA_%s.pdf' foi gerado.\n" +
                        "3. Notificações enviadas para:\n" +
                        "   - Aluno: %s (E-mail enviado)\n" +
                        "   - Secretaria Acadêmica (Cópia arquivada)\n" +
                        "   - Membros da Banca\n\n" +
                        "RESUMO DA AVALIAÇÃO:\n" +
                        "Título: %s\n" +
                        "Nota Final: %.1f\n" +
                        "Situação: %s\n\n" +
                        "Parecer da Banca:\n%s",
                tcc.autor.replace(" ", "_"),
                tcc.autor,
                tcc.titulo,
                nota,
                statusAprovacao,
                parecer
        );

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Processo de Finalização Concluído");
        alert.setHeaderText("TCC Finalizado e Documentação Emitida");
        alert.setContentText(mensagem);

        // Ajuste visual para garantir que todo o texto apareça
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

    @FXML public void fazerLogout() {
        nav.definirSessao(null);
        nav.irPara(Navegacao.Tela.LOGIN);
    }
}