package br.com.juventude.api_contato.auth.strategy;

public interface OAuth2UserInfo {
    String getId();
    String getNome();
    String getEmail();
    String getFotoUrl();
}