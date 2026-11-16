package br.com.juventude.api_contato.ouvidoria;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/propostas")
public class PropostaController {

    private final PropostaService propostaService;

    public PropostaController(PropostaService propostaService) {
        this.propostaService = propostaService;
    }

    @GetMapping
    public ResponseEntity<List<PropostaResponseDTO>> listarPropostas() {
        return ResponseEntity.ok(propostaService.listarTodas());
    }

    @PostMapping
    public ResponseEntity<PropostaResponseDTO> criarProposta(@Valid @RequestBody PropostaRequestDTO dto,
                                                             Authentication authentication) {
        UUID usuarioId = UUID.fromString(authentication.getName());
        PropostaResponseDTO criada = propostaService.criarProposta(dto, usuarioId);
        return ResponseEntity.status(HttpStatus.CREATED).body(criada);
    }

    @PostMapping("/{id}/apoiar")
    public ResponseEntity<PropostaResponseDTO> apoiarProposta(@PathVariable Long id,
                                                              Authentication authentication) {
        UUID usuarioId = UUID.fromString(authentication.getName());
        PropostaResponseDTO atualizada = propostaService.apoiarProposta(usuarioId, id);
        return ResponseEntity.ok(atualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirProposta(@PathVariable Long id,
                                                Authentication authentication) {
        UUID usuarioId = UUID.fromString(authentication.getName());
        propostaService.excluirProposta(usuarioId, id);
        return ResponseEntity.noContent().build();
    }
}