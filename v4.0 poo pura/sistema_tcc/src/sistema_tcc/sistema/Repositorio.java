package sistema_tcc.sistema;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Memória genérica do sistema.
 */
public class Repositorio<T> {
    private final List<T> memoria = new ArrayList<>();

    public void guardar(T objeto) {
        memoria.add(objeto);
    }

    public T buscar(Predicate<T> criterio) {
        return memoria.stream().filter(criterio).findFirst().orElse(null);
    }

    public List<T> filtrar(Predicate<T> criterio) {
        return memoria.stream().filter(criterio).collect(Collectors.toList());
    }

    public List<T> todos() {
        return new ArrayList<>(memoria);
    }
}