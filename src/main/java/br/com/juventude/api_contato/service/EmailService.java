package br.com.juventude.api_contato.service;

import br.com.juventude.api_contato.dto.ContatoRequestDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    // Lê a variável de ambiente diretamente da nuvem
    @Value("${RESEND_API_KEY}")
    private String resendApiKey;

    public void enviarEmailContato(ContatoRequestDTO dados) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.resend.com/emails";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(resendApiKey);

        // Formatação HTML aprimorada da mensagem
        String corpoHtml = String.format(
            "<h3>Nova solicitação via Portal Juventude</h3>" +
            "<p><strong>Nome:</strong> %s</p>" +
            "<p><strong>E-mail:</strong> %s</p>" +
            "<p><strong>Telefone:</strong> %s</p>" +
            "<p><strong>Assunto:</strong> %s</p>" +
            "<p><strong>Mensagem:</strong><br>%s</p>",
            dados.nome(), dados.email(), dados.telefone(), dados.assunto(), dados.mensagem()
        );

        Map<String, Object> body = new HashMap<>();
        // O e-mail emissor fixo para testes no Resend
        body.put("from", "onboarding@resend.dev"); 
        // O e-mail de destino DEVE ser o mesmo e-mail que você cadastrou no Resend
        body.put("to", "juventudejunqueiro@gmail.com"); 
        body.put("reply_to", dados.email());
        body.put("subject", "[Portal Juventude] " + dados.assunto());
        body.put("html", corpoHtml);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        
        // Dispara a requisição HTTP POST blindada
        restTemplate.postForEntity(url, request, String.class);
    }
}