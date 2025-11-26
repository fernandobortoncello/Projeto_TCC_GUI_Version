package sistema_tcc.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sistema_tcc.dominio.*;
import sistema_tcc.dominio.tipos.*;
import sistema_tcc.dto.TccDTO;
import sistema_tcc.dto.UsuarioLogadoDTO;
import sistema_tcc.repositorio.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tcc")
public class TccController {

    @Autowired private TccRepository tccRepo;
    @Autowired private UsuarioRepository usuarioRepo;

    // --- ALUNO ---
    @PostMapping("/propor")
    public TccDTO proporTema(@RequestHeader("user-id") String alunoId, @RequestBody TccDTO dados) {
        Aluno autor = (Aluno) usuarioRepo.findById(alunoId).orElseThrow();
        Tcc novo = autor.criarProposta(dados.titulo(), "Proposta via Web", AreaConhecimento.ENGENHARIA_SOFTWARE);
        return TccDTO.from(tccRepo.save(novo));
    }

    @GetMapping("/meu-tcc")
    public TccDTO verMeuTcc(@RequestHeader("user-id") String alunoId) {
        Tcc tcc = tccRepo.findByAutorId(alunoId);
        return (tcc != null) ? TccDTO.from(tcc) : null;
    }

    // --- PROFESSOR ---

    @GetMapping("/propostas")
    public List<TccDTO> listarPropostas() {
        return tccRepo.findByStatus(TccStatus.PROPOSTA).stream()
                .map(TccDTO::from).collect(Collectors.toList());
    }

    @GetMapping("/meus-orientandos")
    public List<TccDTO> listarOrientandos(@RequestHeader("user-id") String profId) {
        return tccRepo.findByOrientadorId(profId).stream()
                .map(TccDTO::from).collect(Collectors.toList());
    }

    @GetMapping("/professores")
    public List<UsuarioLogadoDTO> listarProfessores() {
        // Retorna lista de professores para compor banca
        return usuarioRepo.findAllByPapel(Papel.PROFESSOR).stream()
                .map(u -> new UsuarioLogadoDTO(u.getId(), u.getNome(), u.getPapel()))
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
    public void registrarOrientacao(@RequestHeader("user-id") String profId,
                                    @PathVariable String idTcc,
                                    @RequestBody String texto) {
        Professor prof = (Professor) usuarioRepo.findById(profId).orElseThrow();
        Tcc tcc = tccRepo.findById(idTcc).orElseThrow();
        tcc.receberOrientacao(prof, texto);
        tccRepo.save(tcc);
    }

    @PostMapping("/{idTcc}/banca")
    public void definirBanca(@RequestHeader("user-id") String profId,
                             @PathVariable String idTcc,
                             @RequestBody Map<String, Object> dadosBanca) {
        Professor prof = (Professor) usuarioRepo.findById(profId).orElseThrow();
        Tcc tcc = tccRepo.findById(idTcc).orElseThrow();

        LocalDate data = LocalDate.parse((String) dadosBanca.get("data"));
        List<String> idsMembros = (List<String>) dadosBanca.get("membros");

        List<Professor> membros = idsMembros.stream()
                .map(id -> (Professor) usuarioRepo.findById(id).orElseThrow())
                .collect(Collectors.toList());

        tcc.agendarBanca(prof, membros, data);
        tccRepo.save(tcc);
    }

    @PostMapping("/{idTcc}/finalizar")
    public void finalizar(@RequestHeader("user-id") String profId,
                          @PathVariable String idTcc,
                          @RequestBody Map<String, Object> dadosNota) {
        Professor prof = (Professor) usuarioRepo.findById(profId).orElseThrow();
        Tcc tcc = tccRepo.findById(idTcc).orElseThrow();

        Double nota = Double.valueOf(dadosNota.get("nota").toString());
        String parecer = (String) dadosNota.get("parecer");

        tcc.receberNotaFinal(prof, nota, parecer);
        tccRepo.save(tcc);
    }
}