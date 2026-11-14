package br.com.juventude.api_contato.ouvidoria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PropostaRequestDTO(
        @NotBlank(message = "O título é obrigatório.")
        @Size(min = 5, max = 150, message = "O título deve ter entre 5 e 150 caracteres.")
        String titulo,

        @NotBlank(message = "A descrição da ideia é obrigatória.")
        @Size(min = 15, max = 2000, message = "A descrição deve ter entre 15 e 2000 caracteres.")
        String descricao
) {}