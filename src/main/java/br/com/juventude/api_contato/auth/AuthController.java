package br.com.juventude.api_contato.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/logout")
    public ResponseEntity> logout() {
        return ResponseEntity.ok(Map.of("mensagem", "Sessão encerrada com sucesso."));
    }
}