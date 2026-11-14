package br.com.juventude.api_contato.exception;

public class ApoioDuplicadoException extends RuntimeException {
    public ApoioDuplicadoException(String mensagem) {
        super(mensagem);
    }
}