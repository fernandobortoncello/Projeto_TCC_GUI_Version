package sistema_tcc;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import sistema_tcc.dominio.*;
import sistema_tcc.dominio.estado.TccCriadoAtivo;
import sistema_tcc.repositorio.TccRepository;
import sistema_tcc.repositorio.UsuarioRepository;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class TccApplication {

    public static void main(String[] args) {
        SpringApplication.run(TccApplication.class, args);
    }

    /**
     * Bean para carga inicial de dados no banco H2.
     * Útil para testes manuais e desenvolvimento, evitando ter que cadastrar tudo do zero a cada restart.
     */
    @Bean
    public CommandLineRunner demo(UsuarioRepository usuarioRepo, TccRepository tccRepo) {
        return (args) -> {
            System.out.println("--- INICIALIZANDO SISTEMA TCC V7.0 (State Pattern) ---");

            // 1. Criar Usuários (Atores do Sistema)
            // Alunos
            Aluno a1 = new Aluno("2025001", "Aline Espindola", "123");
            Aluno a2 = new Aluno("2025002", "Fernando Bortoncello", "123");
            Aluno a3 = new Aluno("2025003", "João da Silva", "123");

            // Professores
            Professor p1 = new Professor("111222", "Willian Bolzan", "123");
            Professor p2 = new Professor("333444", "Thiago Oliveira", "123");
            Professor p3 = new Professor("555666", "Tiago Carvalho", "123"); // Coordenador

            usuarioRepo.saveAll(Arrays.asList(a1, a2, a3, p1, p2, p3));
            System.out.println("-> Usuários cadastrados com sucesso.");

            // 2. Criar TCCs com Estados Iniciais Diferenciados

            // TCC 1: Recém criado (Proposta) - Estado: TccCriadoAtivo
           // Tcc tcc1 = new Tcc(a1, "Arquitetura Pure OO", "Estudo sobre a filosofia de Alan Kay aplicada em sistemas modernos.");
            // O construtor do TCC já define o estado inicial como TccCriadoAtivo
           // tccRepo.save(tcc1);
           // System.out.println("-> TCC 1 criado (Estado: Proposta).");

            // TCC 2: Aceito por um orientador - Estado: TccAceitoAtivo
            //Tcc tcc2 = new Tcc(a2, "Inteligência Artificial na Educação", "Uso de LLMs para tutoria.");
            // Simulando o fluxo de aceite
          //  tcc2.aceitarOrientador(p1);
           // tccRepo.save(tcc2);
           // System.out.println("-> TCC 2 criado e aceito por orientador (Estado: Aceito/Em Andamento).");

            // TCC 3: Em andamento com orientações - Estado: TccSobOrientacaoAtivo
            // Para chegar neste estado, precisa ter sido aceito e ter recebido uma orientação
            // (Isso exigiria instanciar um TCC, aceitar e registrar orientação, mas para o demo simples
            // vamos deixar os estados iniciais mais comuns).

            System.out.println("--- CARGA DE DADOS CONCLUÍDA ---");
            System.out.println("Acesse a aplicação em: http://localhost:8080");
            System.out.println("Console H2: http://localhost:8080/h2-console");
        };
    }
}