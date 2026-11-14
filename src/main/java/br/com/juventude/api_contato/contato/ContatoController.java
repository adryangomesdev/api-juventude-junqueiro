package br.com.juventude.api_contato.controller;

import br.com.juventude.api_contato.dto.ContatoRequestDTO;
import br.com.juventude.api_contato.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contato")
@CrossOrigin(origins = "https://portal-juventude-junqueiro.vercel.app")
public class ContatoController {

    private final EmailService emailService;

    public ContatoController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity<String> receberContato(@Valid @RequestBody ContatoRequestDTO request) {
        try {
            emailService.enviarEmailContato(request);
            return ResponseEntity.ok().body("Mensagem processada e enviada com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Falha na comunicação com o servidor SMTP.");
        }
    }
}