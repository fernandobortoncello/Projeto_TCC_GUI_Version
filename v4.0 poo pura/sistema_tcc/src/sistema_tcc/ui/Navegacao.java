package sistema_tcc.ui;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;
import sistema_tcc.AppJavaFX;
import sistema_tcc.sistema.sessao.Sessao;
import java.util.*;

public class Navegacao {
    public enum Tela { LOGIN, ALUNO, PROFESSOR }
    private final Stage stage;
    private final Map<Tela, Object> ctrls = new HashMap<>();
    private Sessao sessaoAtual; // Estado global da sessão

    public Navegacao(Stage s) { this.stage = s; }
    public void registrar(Tela t, Object c) { ctrls.put(t, c); }
    public void definirSessao(Sessao s) { this.sessaoAtual = s; }
    public Sessao lerSessao() { return sessaoAtual; }

    public void irPara(Tela t) {
        try {
            String fxml = t == Tela.LOGIN ? "vista/LoginVista.fxml" :
                    t == Tela.ALUNO ? "vista/AlunoVista.fxml" : "vista/ProfessorVista.fxml";
            FXMLLoader l = new FXMLLoader(AppJavaFX.class.getResource("ui/" + fxml));
            l.setController(ctrls.get(t));
            Parent r = l.load();
            if(stage.getScene()==null) stage.setScene(new Scene(r, 800, 600));
            else stage.getScene().setRoot(r);
            stage.show();
        } catch(Exception e) { e.printStackTrace(); }
    }
}