package br.com.juventude.api_contato.auth;

import br.com.juventude.api_contato.auth.strategy.FacebookUserInfo;
import br.com.juventude.api_contato.auth.strategy.GoogleUserInfo;
import br.com.juventude.api_contato.auth.strategy.OAuth2UserInfo;
import br.com.juventude.api_contato.usuario.ProvedorAuthEnum;
import br.com.juventude.api_contato.usuario.Usuario;
import br.com.juventude.api_contato.usuario.UsuarioService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UsuarioService usuarioService;

    public CustomOAuth2UserService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId().toUpperCase();

        ProvedorAuthEnum provider = ProvedorAuthEnum.valueOf(registrationId);
        OAuth2UserInfo userInfo = switch (provider) {
            case GOOGLE -> new GoogleUserInfo(oAuth2User.getAttributes());
            case FACEBOOK -> new FacebookUserInfo(oAuth2User.getAttributes());
        };

        Usuario usuario = usuarioService.salvarOuAtualizarOAuth(userInfo, provider);

        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
        attributes.put("appUserId", usuario.getId().toString());

        String nameAttributeKey = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        return new DefaultOAuth2User(oAuth2User.getAuthorities(), attributes, nameAttributeKey);
    }
}