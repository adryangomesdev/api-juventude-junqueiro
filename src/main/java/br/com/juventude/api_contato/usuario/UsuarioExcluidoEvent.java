package br.com.juventude.api_contato.usuario;

import java.util.UUID;

public record UsuarioExcluidoEvent(UUID usuarioId) {
}