package sistema_tcc.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sistema_tcc.AppJavaFX;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Gerencia a navegação entre as telas (Scenes).
 * Esta classe é crucial para a Injeção de Dependência, pois ela
 * associa as instâncias dos Controladores (criadas no AppJavaFX)
 * com os arquivos FXML corretos no momento da carga.
 */
public class Navegacao {

    public enum Tela {
        LOGIN("vista/LoginVista.fxml"),
        ALUNO_DASHBOARD("vista/AlunoVista.fxml"),
        PROFESSOR_DASHBOARD("vista/ProfessorVista.fxml");

        private final String fxml;
        Tela(String fxml) { this.fxml = fxml; }
        public String getFxml() { return fxml; }
    }

    private final Stage stagePrincipal;
    private final Map<Tela, Object> controladores = new HashMap<>();

    public Navegacao(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }

    /**
     * Chamado pelo AppJavaFX para registrar os controladores instanciados.
     */
    public void registrarControlador(Tela tela, Object controlador) {
        controladores.put(tela, controlador);
    }

    /**
     * Carrega e exibe uma nova tela.
     */
    public void navegarPara(Tela tela) {
        try {
            // 1. Busca o controlador que já foi instanciado e injetado
            Object controlador = controladores.get(tela);
            if (controlador == null) {
                throw new IllegalStateException("Controlador não registrado para a tela: " + tela);
            }

            // 2. Encontra o arquivo FXML
            URL fxmlUrl = AppJavaFX.class.getResource("ui/" + tela.getFxml());
            if (fxmlUrl == null) {
                throw new IOException("Não foi possível encontrar o FXML: " + tela.getFxml());
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);

            // 3. ATRIBUI O CONTROLADOR MANUALMENTE (Injeção de Dependência)
            // Isso previne o FXML de tentar criar uma nova instância
            loader.setController(controlador);

            // 4. Carrega a tela
            Parent root = loader.load();

            // 5. Exibe a cena
            Scene scene = stagePrincipal.getScene();
            if (scene == null) {
                // Define um tamanho padrão para a primeira cena
                stagePrincipal.setScene(new Scene(root, 800, 600));
            } else {
                stagePrincipal.getScene().setRoot(root);
            }
            stagePrincipal.show();

        } catch (IOException | IllegalStateException e) {
            System.err.println("Falha ao navegar para a tela: " + tela);
            e.printStackTrace();
        }
    }
}