package sistema_tcc.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import sistema_tcc.dominio.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {}