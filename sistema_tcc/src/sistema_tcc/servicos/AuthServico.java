package sistema_tcc.servicos;

import sistema_tcc.dominio.Aluno;
import sistema_tcc.dominio.Professor;
import sistema_tcc.dominio.Usuario;
import sistema_tcc.repositorio.AlunoRepositorio;
import sistema_tcc.repositorio.ProfessorRepositorio;

/**
 * Serviço de Autenticação (Caso de Uso: Login).
 */
public class AuthServico {

    private final AlunoRepositorio alunoRepositorio;
    private final ProfessorRepositorio professorRepositorio;
    private final SessaoUsuario sessaoUsuario;

    public AuthServico(AlunoRepositorio ar, ProfessorRepositorio pr, SessaoUsuario sessao) {
        this.alunoRepositorio = ar;
        this.professorRepositorio = pr;
        this.sessaoUsuario = sessao;
    }

    /**
     * Tenta logar um usuário.
     * @return O Usuário logado (Aluno ou Professor).
     * @throws AuthException Se o login falhar.
     */
    public Usuario login(String idUsuario, String senha) throws AuthException {
        // Tenta como Aluno (usando matrícula)
        Aluno aluno = alunoRepositorio.buscarPorMatricula(idUsuario);
        if (aluno != null) {
            if (aluno.verificarSenha(senha)) {
                sessaoUsuario.setUsuarioLogado(aluno); // Define o usuário na sessão
                return aluno;
            } else {
                throw new AuthException("Senha incorreta."); // RNF002
            }
        }

        // Tenta como Professor (usando ID/CPF)
        Professor prof = professorRepositorio.buscarPorId(idUsuario);
        if (prof != null) {
            if (prof.verificarSenha(senha)) {
                sessaoUsuario.setUsuarioLogado(prof); // Define o usuário na sessão
                return prof;
            } else {
                throw new AuthException("Senha incorreta."); // RNF002
            }
        }

        throw new AuthException("Usuário (CPF/Matrícula) não encontrado.");
    }
}