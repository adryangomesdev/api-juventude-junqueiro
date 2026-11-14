package br.com.juventude.api_contato.ouvidoria.observer;

import br.com.juventude.api_contato.contato.ContatoRequestDTO;
import br.com.juventude.api_contato.contato.EmailService;
import br.com.juventude.api_contato.ouvidoria.Proposta;
import org.springframework.stereotype.Component;

@Component
public class EmailSecretariaObserver implements PropostaObserver {

    private final EmailService emailService;

    public EmailSecretariaObserver(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void notificarMetaAtingida(Proposta proposta, long totalApoios) {
        String assunto = "[Banco de Ideias] Proposta atingiu " + totalApoios + " apoios: " + proposta.getTitulo();
        String mensagem = String.format(
                "A proposta #%d ('%s'), criada por %s, atingiu a meta de %d apoios e entrou automaticamente em análise pela Secretaria.\n\nDescrição da proposta:\n%s",
                proposta.getId(),
                proposta.getTitulo(),
                proposta.getUsuario().getNome(),
                totalApoios,
                proposta.getDescricao()
        );

        ContatoRequestDTO alerta = new ContatoRequestDTO(
                "Sistema Ouvidoria Juventude",
                proposta.getUsuario().getEmail(),
                "Não informado",
                assunto,
                mensagem
        );

        // Chamando o método exato do seu EmailService
        emailService.enviarEmailContato(alerta);
    }
}