package br.com.juventude.api_contato.ouvidoria;

import java.time.LocalDateTime;

public record PropostaResponseDTO(
        Long id,
        String titulo,
        String descricao,
        StatusProposta status,
        LocalDateTime dataCriacao,
        String autorNome,
        String autorFotoUrl,
        long totalApoios
) {
    public static PropostaResponseDTO fromEntity(Proposta p, long totalApoios) {
        return new PropostaResponseDTO(
                p.getId(),
                p.getTitulo(),
                p.getDescricao(),
                p.getStatus(),
                p.getDataCriacao(),
                p.getUsuario().getNome(),
                p.getUsuario().getFotoUrl(),
                totalApoios
        );
    }
}