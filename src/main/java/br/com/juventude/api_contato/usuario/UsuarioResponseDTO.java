package br.com.juventude.api_contato.usuario;

import java.util.UUID;

public record UsuarioResponseDTO(
        UUID id,
        String nome,
        String email,
        String fotoUrl,
        RoleEnum role
) {
    public static UsuarioResponseDTO fromEntity(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getFotoUrl(),
                usuario.getRole()
        );
    }
}