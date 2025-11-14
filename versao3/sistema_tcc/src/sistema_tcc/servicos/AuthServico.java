package sistema_tcc.servicos;

import sistema_tcc.dominio.Aluno;
import sistema_tcc.dominio.Professor;
import sistema_tcc.dominio.Usuario;
import sistema_tcc.repositorio.AlunoRepositorio;
import sistema_tcc.repositorio.ProfessorRepositorio;
import sistema_tcc.servicos.dto.UsuarioLogadoDTO;

/**
 * Serviço de Autenticação (Caso de Uso: Login).
 * Refatorado para retornar um DTO, não um objeto de domínio.
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
     * @return O DTO do Usuário logado.
     * @throws AuthException Se o login falhar.
     */
    public UsuarioLogadoDTO login(String idUsuario, String senha) throws AuthException {
        Usuario usuario = null;

        // Tenta como Aluno (usando matrícula)
        Aluno aluno = alunoRepositorio.buscarPorMatricula(idUsuario);
        if (aluno != null) {
            usuario = aluno;
        } else {
            // Tenta como Professor (usando ID/CPF)
            Professor prof = professorRepositorio.buscarPorId(idUsuario);
            if (prof != null) {
                usuario = prof;
            }
        }

        if (usuario == null) {
            throw new AuthException("Usuário (CPF/Matrícula) não encontrado.");
        }

        // (CORREÇÃO DE ERRO 1 & 2)
        if (usuario.verificarSenha(senha)) {
            // Sucesso no login
            // Mapeia o objeto de domínio para um DTO
            UsuarioLogadoDTO dto = new UsuarioLogadoDTO(usuario.getId(), usuario.getNome(), usuario.getPapel());
            sessaoUsuario.setUsuarioLogado(dto); // Define o DTO na sessão
            return dto; // Retorna o DTO
        } else {
            throw new AuthException("Senha incorreta."); // RNF002
        }
    }
}