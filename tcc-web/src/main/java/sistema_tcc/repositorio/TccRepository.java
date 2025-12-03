package sistema_tcc.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sistema_tcc.dominio.Tcc;
import java.util.List;

public interface TccRepository extends JpaRepository<Tcc, String> {
    @Query("SELECT t FROM Tcc t WHERE t.orientador.id = :profId")
    List<Tcc> findByOrientadorId(String profId);

    @Query("SELECT t FROM Tcc t WHERE t.autor.id = :alunoId")
    List<Tcc> findAllByAutorId(String alunoId);
}