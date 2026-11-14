package br.com.juventude.api_contato.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ContatoRequestDTO(
    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 100)
    String nome,

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    String email,

    @NotBlank(message = "O telefone é obrigatório")
    @Size(max = 20)
    String telefone,

    @NotBlank(message = "O assunto é obrigatório")
    String assunto,

    @NotBlank(message = "A mensagem não pode estar vazia")
    @Size(max = 2000, message = "A mensagem excede o limite permitido de 2000 caracteres")
    String mensagem
) {}