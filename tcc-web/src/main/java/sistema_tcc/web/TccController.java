package sistema_tcc.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sistema_tcc.dominio.*;
import sistema_tcc.dominio.estado.TccCriadoAtivo;
import sistema_tcc.dominio.estado.TccFinalizado;
import sistema_tcc.dto.TccDTO;
import sistema_tcc.dto.UsuarioLogadoDTO;
import sistema_tcc.repositorio.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tcc")
public class TccController {

    @Autowired private TccRepository tccRepo;
    @Autowired private UsuarioRepository usuarioRepo;

    // ALUNO
    @PostMapping("/propor")
    public TccDTO proporTema(@RequestHeader("user-id") String alunoId, @RequestBody TccDTO dados) {
        Aluno autor = (Aluno) usuarioRepo.findById(alunoId).orElseThrow();

        // CORREÇÃO: Impede criação se houver TCC Ativo OU Finalizado
        List<Tcc> tccs = tccRepo.findAllByAutorId(alunoId);
        boolean existeTcc = tccs.stream().anyMatch(t -> t.isAtivo() || t.getEstado() instanceof TccFinalizado);

        if (existeTcc) {
            throw new IllegalStateException("Aluno já possui registro de TCC.");
        }

        Tcc novo = new Tcc(autor, dados.titulo(), dados.descricao());
        tccRepo.save(novo);
        return novo.gerarSnapshot();
    }

    @GetMapping("/meu-tcc")
    public TccDTO verMeuTcc(@RequestHeader("user-id") String alunoId) {
        List<Tcc> tccs = tccRepo.findAllByAutorId(alunoId);
        if (tccs.isEmpty()) return null;

        // Retorna o último TCC (assumindo ser o relevante), mesmo se finalizado
        return tccs.get(tccs.size() - 1).gerarSnapshot();
    }

    // PROFESSOR
    @GetMapping("/propostas")
    public List<TccDTO> listarPropostas() {
        return tccRepo.findAll().stream()
                .filter(t -> t.getEstado() instanceof TccCriadoAtivo)
                .map(Tcc::gerarSnapshot)
                .collect(Collectors.toList());
    }

    @GetMapping("/meus-orientandos")
    public List<TccDTO> listarOrientandos(@RequestHeader("user-id") String profId) {
        return tccRepo.findByOrientadorId(profId).stream()
                .map(Tcc::gerarSnapshot)
                .collect(Collectors.toList());
    }

    @PostMapping("/{idTcc}/orientar")
    public void assumirOrientacao(@RequestHeader("user-id") String profId, @PathVariable String idTcc) {
        Professor prof = (Professor) usuarioRepo.findById(profId).orElseThrow();
        Tcc tcc = tccRepo.findById(idTcc).orElseThrow();
        tcc.aceitarOrientador(prof);
        tccRepo.save(tcc);
    }

    @PostMapping("/{idTcc}/orientacao")
    public void registrarOrientacao(@RequestHeader("user-id") String profId, @PathVariable String idTcc, @RequestBody Map<String, String> dados) {
        Professor prof = (Professor) usuarioRepo.findById(profId).orElseThrow();
        Tcc tcc = tccRepo.findById(idTcc).orElseThrow();
        String data = dados.get("data");
        String descricao = dados.get("descricao");

        // CORREÇÃO: O input type="date" envia no formato yyyy-MM-dd.
        // Fazemos o parse desse formato e formatamos para exibição.
        LocalDate dataObj = LocalDate.parse(data);
        String texto = dataObj.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ": " + descricao;

        tcc.receberOrientacao(prof, texto);
        tccRepo.save(tcc);
    }

    @PostMapping("/{idTcc}/banca")
    public void definirBanca(@RequestHeader("user-id") String profId, @PathVariable String idTcc, @RequestBody Map<String, Object> dadosBanca) {
        Professor prof = (Professor) usuarioRepo.findById(profId).orElseThrow();
        Tcc tcc = tccRepo.findById(idTcc).orElseThrow();
        String data = (String) dadosBanca.get("data");
        List<String> ids = (List<String>) dadosBanca.get("membros");
        List<Professor> membros = ids.stream().map(id -> (Professor) usuarioRepo.findById(id).orElseThrow()).collect(Collectors.toList());

        // CORREÇÃO: Mesmo ajuste para a data da banca
        LocalDate dataObj = LocalDate.parse(data);
        tcc.agendarBanca(prof, membros, dataObj.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        tccRepo.save(tcc);
    }

    @PostMapping("/{idTcc}/finalizar")
    public ResponseEntity<?> finalizar(@RequestHeader("user-id") String profId, @PathVariable String idTcc, @RequestBody Map<String, Object> dadosNota) {
        try {
            Professor prof = (Professor) usuarioRepo.findById(profId).orElseThrow();
            Tcc tcc = tccRepo.findById(idTcc).orElseThrow();
            Double nota = Double.valueOf(dadosNota.get("nota").toString());

            if (nota < 0 || nota > 10) return ResponseEntity.badRequest().body("Erro: Nota deve ser entre 0 e 10.");

            tcc.receberNotaFinal(prof, nota, (String) dadosNota.get("parecer"));
            tccRepo.save(tcc);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @GetMapping("/{idTcc}/ata")
    public ResponseEntity<byte[]> baixarAta(@PathVariable String idTcc) {
        Tcc tcc = tccRepo.findById(idTcc).orElseThrow();
        byte[] pdfBytes = AtaGenerator.gerarAtaPdf(tcc);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ata_" + tcc.getId() + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    // UTILS
    @GetMapping("/usuarios")
    public List<UsuarioLogadoDTO> listarTodosUsuarios() {
        return usuarioRepo.findAll().stream().map(u -> new UsuarioLogadoDTO(u.getId(), u.getNome(), u.getClass().getSimpleName())).collect(Collectors.toList());
    }

    @GetMapping("/professores")
    public List<UsuarioLogadoDTO> listarProfessores() {
        return usuarioRepo.findAll().stream().filter(u -> u instanceof Professor).map(u -> new UsuarioLogadoDTO(u.getId(), u.getNome(), "PROFESSOR")).collect(Collectors.toList());
    }
}