package br.com.juventude.api_contato.exception;

public class PropostaFechadaException extends RuntimeException {
    public PropostaFechadaException(String mensagem) {
        super(mensagem);
    }
}