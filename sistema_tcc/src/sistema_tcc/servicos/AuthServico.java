package sistema_tcc.servicos; // <-- CORRIGIDO: Removido o "java."

import sistema_tcc.dominio.Aluno;
import sistema_tcc.dominio.Papel; // Importação necessária
import sistema_tcc.dominio.Professor;
import sistema_tcc.dominio.Usuario;
import sistema_tcc.repositorio.AlunoRepositorio;
import sistema_tcc.repositorio.ProfessorRepositorio;
// java.util.Optional não é mais necessário aqui

/**
 * Serviço de Autenticação (Caso de Uso: Login).
 * Desacoplado da UI. Lança exceções em caso de falha.
 * ESTA VERSÃO CORRIGE AS CHAMADAS DE REPOSITÓRIO.
 */
public class AuthServico {

    private final AlunoRepositorio alunoRepositorio;
    private final ProfessorRepositorio professorRepositorio;

    public AuthServico(AlunoRepositorio ar, ProfessorRepositorio pr) {
        this.alunoRepositorio = ar;
        this.professorRepositorio = pr;
    }

    /**
     * Tenta logar um usuário.
     * @return O Usuário logado (Aluno ou Professor).
     * @throws AuthException Se o login falhar.
     */
    public Usuario login(String cpfOuMatricula, String senha) throws AuthException {

        // Tenta como Aluno
        // CORRIGIDO: Chamando o método que existe em AlunoRepositorio
        Aluno aluno = alunoRepositorio.buscarPorMatricula(cpfOuMatricula);

        if (aluno != null) { // Se encontrou um aluno
            if (aluno.verificarSenha(senha)) {
                System.out.println("LOG: Login Aluno OK: " + aluno.getNome());
                return aluno;
            } else {
                throw new AuthException("Senha incorreta."); // RNF002
            }
        }

        // Tenta como Professor
        // CORRIGIDO: Chamando o método que existe em ProfessorRepositorio
        Professor prof = professorRepositorio.buscarPorId(cpfOuMatricula);

        if (prof != null) { // Se encontrou um professor
            if (prof.verificarSenha(senha)) {
                System.out.println("LOG: Login Professor OK: " + prof.getNome());
                return prof;
            } else {
                throw new AuthException("Senha incorreta."); // RNF002
            }
        }

        // Se não encontrou nem aluno nem professor
        throw new AuthException("Usuário (CPF/Matrícula) não encontrado.");
    }
}