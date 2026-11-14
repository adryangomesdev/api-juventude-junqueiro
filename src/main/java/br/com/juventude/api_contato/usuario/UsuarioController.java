package br.com.juventude.api_contato.usuario;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> obterPerfilLogado(Authentication authentication) {
        UUID usuarioId = UUID.fromString(authentication.getName());
        Usuario usuario = usuarioService.buscarPorId(usuarioId);
        return ResponseEntity.ok(UsuarioResponseDTO.fromEntity(usuario));
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deletarConta(Authentication authentication) {
        UUID usuarioId = UUID.fromString(authentication.getName());
        usuarioService.excluirConta(usuarioId);
        return ResponseEntity.noContent().build();
    }
}