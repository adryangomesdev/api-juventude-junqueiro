package br.com.juventude.api_contato.exception;

import java.time.LocalDateTime;

public record ErroDTO(
        String codigo,
        String mensagem,
        LocalDateTime timestamp
) {
    public ErroDTO(String codigo, String mensagem) {
        this(codigo, mensagem, LocalDateTime.now());
    }
}