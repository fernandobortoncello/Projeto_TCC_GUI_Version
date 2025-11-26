package sistema_tcc;
import javafx.application.Application;
import javafx.stage.Stage;
import sistema_tcc.sistema.Sistema;
import sistema_tcc.ui.Navegacao;
import sistema_tcc.ui.controlador.*;

public class AppJavaFX extends Application {
    @Override public void start(Stage stage) {
        Sistema sistema = new Sistema(); // O objeto "Computador"
        Navegacao nav = new Navegacao(stage);

        nav.registrar(Navegacao.Tela.LOGIN, new LoginControlador(sistema, nav));
        nav.registrar(Navegacao.Tela.ALUNO, new AlunoControlador(nav));
        nav.registrar(Navegacao.Tela.PROFESSOR, new ProfessorControlador(nav));

        stage.setTitle("Sistema TCC V5.0 (Pure Kay OO)");
        nav.irPara(Navegacao.Tela.LOGIN);
    }
    public static void main(String[] args) { launch(args); }
}