package sistema_tcc.servicos;

import sistema_tcc.servicos.dto.UsuarioLogadoDTO;
import sistema_tcc.dominio.Papel;

/**
 * Gerencia o estado de login do usuário (Singleton).
 * Refatorado para armazenar o UsuarioLogadoDTO.
 */
public class SessaoUsuario {

    // Armazena o DTO, não o objeto de domínio
    private UsuarioLogadoDTO usuarioLogado;

    public void setUsuarioLogado(UsuarioLogadoDTO usuario) {
        this.usuarioLogado = usuario;
    }

    public void logout() {
        this.usuarioLogado = null;
    }

    /**
     * Busca o DTO do usuário logado.
     * @throws IllegalStateException Se nenhum usuário estiver logado.
     */
    public UsuarioLogadoDTO getUsuarioLogado() {
        if (usuarioLogado == null) {
            throw new IllegalStateException("Nenhum usuário está logado.");
        }
        return usuarioLogado;
    }

    /**
     * Verifica se o usuário logado é de um tipo específico (Aluno/Professor).
     */
    public UsuarioLogadoDTO getUsuarioLogado(Papel papel) {
        UsuarioLogadoDTO dto = getUsuarioLogado();
        if (dto.getPapel() != papel) {
            throw new IllegalStateException("O usuário logado não é do tipo " + papel.name());
        }
        return dto;
    }
}