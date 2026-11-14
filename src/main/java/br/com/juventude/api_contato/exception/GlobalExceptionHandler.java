package br.com.juventude.api_contato.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApoioDuplicadoException.class)
    public ResponseEntity<ErroDTO> handleApoioDuplicado(ApoioDuplicadoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErroDTO("APOIO_DUPLICADO", ex.getMessage()));
    }

    @ExceptionHandler(PropostaFechadaException.class)
    public ResponseEntity<ErroDTO> handlePropostaFechada(PropostaFechadaException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErroDTO("PROPOSTA_ENCERRADA", ex.getMessage()));
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroDTO> handleNaoEncontrado(RecursoNaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErroDTO("RECURSO_NAO_ENCONTRADO", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroDTO> handleValidacao(MethodArgumentNotValidException ex) {
        String mensagem = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .findFirst()
                .orElse("Dados inválidos na requisição.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErroDTO("DADOS_INVALIDOS", mensagem));
    }
}