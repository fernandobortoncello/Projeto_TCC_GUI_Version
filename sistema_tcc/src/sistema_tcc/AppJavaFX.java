package sistema_tcc;

import javafx.application.Application;
import javafx.stage.Stage;
import sistema_tcc.infra.MockAlunoRepositorio;
import sistema_tcc.infra.MockProfessorRepositorio;
import sistema_tcc.infra.MockTccRepositorio;
import sistema_tcc.repositorio.AlunoRepositorio;
import sistema_tcc.repositorio.ProfessorRepositorio;
import sistema_tcc.repositorio.TccRepositorio;
import sistema_tcc.servicos.AuthServico;
import sistema_tcc.servicos.TccServico;
import sistema_tcc.ui.Navegacao;
import sistema_tcc.ui.controlador.AlunoControlador;
import sistema_tcc.ui.controlador.LoginControlador;
import sistema_tcc.ui.controlador.ProfessorControlador;

/**
 * Ponto de Entrada da Aplicação JavaFX.
 * * Responsabilidade: Configurar a Injeção de Dependências e
 * iniciar a primeira tela.
 */
public class AppJavaFX extends Application {

    @Override
    public void start(Stage stagePrincipal) {

        // --- 1. Camada de Infraestrutura (Repositórios) ---
        // Em um app real, seriam implementações JDBC/JPA.
        // CORREÇÃO: Usamos as implementações Mock, não as interfaces
        MockAlunoRepositorio alunoRepositorio = new MockAlunoRepositorio();
        MockProfessorRepositorio professorRepositorio = new MockProfessorRepositorio();
        TccRepositorio tccRepositorio = new MockTccRepositorio();

        // Popular com dados de exemplo
        // Agora estes métodos existem
        alunoRepositorio.popularDadosDemo();
        professorRepositorio.popularDadosDemo();

        // --- 2. Camada de Serviços (Lógica da Aplicação) ---
        AuthServico authServico = new AuthServico(alunoRepositorio, professorRepositorio);
        TccServico tccServico = new TccServico(tccRepositorio);

        // --- 3. Camada de UI (Configuração) ---
        Navegacao navegacao = new Navegacao(stagePrincipal);

        // Instanciamos os controladores manualmente, injetando suas dependências
        LoginControlador loginControlador = new LoginControlador(authServico, navegacao);
        AlunoControlador alunoControlador = new AlunoControlador(tccServico, navegacao);
        ProfessorControlador professorControlador = new ProfessorControlador(tccServico, navegacao);

        // Registramos os controladores no serviço de navegação
        navegacao.registrarControlador(Navegacao.Tela.LOGIN, loginControlador);
        navegacao.registrarControlador(Navegacao.Tela.ALUNO_DASHBOARD, alunoControlador);
        navegacao.registrarControlador(Navegacao.Tela.PROFESSOR_DASHBOARD, professorControlador);

        // --- 4. Iniciar ---
        stagePrincipal.setTitle("Sistema de Gerenciamento de TCC");
        navegacao.navegarPara(Navegacao.Tela.LOGIN);
    }

    public static void main(String[] args) {
        launch(args);
    }
}