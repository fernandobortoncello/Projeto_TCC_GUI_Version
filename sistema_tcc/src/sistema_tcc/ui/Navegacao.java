package sistema_tcc.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import sistema_tcc.ui.controlador.AlunoControlador;
import sistema_tcc.ui.controlador.LoginControlador;
import sistema_tcc.ui.controlador.ProfessorControlador;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Serviço central de navegação.
 * Armazena os caminhos FXML e as instâncias dos controladores.
 *
 * CORREÇÃO: O método navegarPara() foi atualizado para
 * injetar a instância do controlador no FXMLLoader antes de carregar o FXML.
 * Esta é a correção para o erro 'No controller specified'.
 */
public class Navegacao {

    public enum Tela {
        LOGIN,
        ALUNO_DASHBOARD,
        PROFESSOR_DASHBOARD
    }

    private final Stage stagePrincipal;
    private Scene cenaPrincipal;

    // Armazena os caminhos para os FXMLs
    private final Map<Tela, String> telas = new HashMap<>();

    // Armazena as INSTÂNCIAS dos controladores (injetadas pelo AppJavaFX)
    private final Map<Tela, Object> controladores = new HashMap<>();

    public Navegacao(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
        configurarRotas();
    }

    private void configurarRotas() {
        telas.put(Tela.LOGIN, "/sistema_tcc/ui/vista/LoginVista.fxml");
        telas.put(Tela.ALUNO_DASHBOARD, "/sistema_tcc/ui/vista/AlunoVista.fxml");
        telas.put(Tela.PROFESSOR_DASHBOARD, "/sistema_tcc/ui/vista/ProfessorVista.fxml");
    }

    /**
     * Chamado pelo AppJavaFX para registrar as instâncias dos controladores.
     */
    public void registrarControlador(Tela tela, Object controlador) {
        if (controlador == null) {
            throw new IllegalArgumentException("Controlador não pode ser nulo para tela: " + tela);
        }
        controladores.put(tela, controlador);
    }

    /**
     * O método central que carrega e exibe uma nova tela.
     */
    public void navegarPara(Tela tela) {
        try {
            String caminhoFxml = telas.get(tela);
            if (caminhoFxml == null) {
                throw new IOException("Nenhum caminho FXML definido para a tela: " + tela);
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoFxml));

            // --- ESTA É A CORREÇÃO ---
            // 1. Buscar o controlador que o AppJavaFX já instanciou
            Object controlador = controladores.get(tela);
            if (controlador == null) {
                throw new IllegalStateException("Nenhum controlador foi registrado para a tela: " + tela + ". Verifique AppJavaFX.java.");
            }

            // 2. Definir o controlador no loader ANTES de chamar .load()
            loader.setController(controlador);
            // --- FIM DA CORREÇÃO ---

            Parent root = loader.load();

            if (cenaPrincipal == null) {
                cenaPrincipal = new Scene(root);
                stagePrincipal.setScene(cenaPrincipal);
            } else {
                cenaPrincipal.setRoot(root);
            }
            stagePrincipal.show();

        } catch (IOException | IllegalStateException e) {
            e.printStackTrace();
            // Mostrar um erro fatal para o usuário se a navegação falhar
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro Crítico de Navegação");
            alert.setHeaderText("Não foi possível carregar a tela: " + tela);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}