package sistema_tcc.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sistema_tcc.dominio.Usuario;
import sistema_tcc.dominio.Aluno;
import sistema_tcc.dto.UsuarioLogadoDTO;
import sistema_tcc.repositorio.UsuarioRepository;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private UsuarioRepository repo;

    @PostMapping("/login")
    public UsuarioLogadoDTO login(@RequestBody Map<String, String> credenciais) {
        String id = credenciais.get("id");
        String senha = credenciais.get("senha");

        Usuario u = repo.findById(id).orElse(null);

        if (u != null && u.autenticar(senha)) {
            String papel = (u instanceof Aluno) ? "ALUNO" : "PROFESSOR";
            return new UsuarioLogadoDTO(u.getId(), u.getNome(), papel);
        }
        throw new RuntimeException("Login inválido");
    }
}