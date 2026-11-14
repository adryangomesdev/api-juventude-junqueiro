package br.com.juventude.api_contato.auth;

import br.com.juventude.api_contato.usuario.Usuario;
import br.com.juventude.api_contato.usuario.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UsuarioService usuarioService;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public OAuth2SuccessHandler(JwtService jwtService, UsuarioService usuarioService) {
        this.jwtService = jwtService;
        this.usuarioService = usuarioService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        UUID appUserId = UUID.fromString((String) oAuth2User.getAttributes().get("appUserId"));

        Usuario usuario = usuarioService.buscarPorId(appUserId);
        String token = jwtService.gerarToken(usuario);

        String targetUrl = frontendUrl + "/#/oauth-callback?token=" + token;
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}