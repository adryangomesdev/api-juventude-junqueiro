package br.com.juventude.api_contato.service;

import br.com.juventude.api_contato.dto.ContatoRequestDTO;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarEmailContato(ContatoRequestDTO dados) {
        SimpleMailMessage message = new SimpleMailMessage();
        
        message.setTo("juventudejunqueiro@gmail.com");
        message.setSubject("[Portal Juventude] " + dados.assunto());
        
        String corpo = String.format(
            "Nova solicitação de contato via Portal:\n\n" +
            "Nome: %s\nE-mail: %s\nTelefone: %s\nAssunto: %s\n\nMensagem:\n%s",
            dados.nome(), dados.email(), dados.telefone(), dados.assunto(), dados.mensagem()
        );
        
        message.setText(corpo);
        message.setReplyTo(dados.email());

        mailSender.send(message);
    }
}