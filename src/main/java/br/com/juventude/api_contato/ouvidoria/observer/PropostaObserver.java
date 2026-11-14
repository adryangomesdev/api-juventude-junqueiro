package br.com.juventude.api_contato.ouvidoria.observer;

import br.com.juventude.api_contato.ouvidoria.Proposta;

public interface PropostaObserver {
    void notificarMetaAtingida(Proposta proposta, long totalApoios);
}