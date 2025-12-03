package sistema_tcc.dto;

import java.util.List;

public record TccDTO(
        String id,
        String titulo,
        String descricao,
        String status,
        String autor,
        String orientador,
        List<String> orientacoes,
        Double nota,
        String parecer,
        String dataBanca,
        List<String> bancaMembros
) {}