package br.com.juventude.api_contato.config;

import br.com.juventude.api_contato.auth.CustomOAuth2UserService;
import br.com.juventude.api_contato.auth.JwtAuthenticationFilter;
import br.com.juventude.api_contato.auth.OAuth2SuccessHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService,
                          OAuth2SuccessHandler oAuth2SuccessHandler,
                          JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                // Permite sessão temporária apenas para o handshake OAuth2 (parâmetro 'state')
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .authorizeHttpRequests(auth -> auth
                        // Libera preflight CORS antes de qualquer validação de token
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // Rotas 100% públicas (Health Check, Contato, Login Social, leitura das propostas e rota interna de erro)
                        .requestMatchers(HttpMethod.GET, "/api/health", "/api/propostas/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/contato").permitAll()
                        .requestMatchers("/oauth2/**", "/login/**", "/error").permitAll()
                        // Todas as demais rotas (criar proposta, apoiar, perfil, excluir conta) exigem JWT válido
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .successHandler(oAuth2SuccessHandler)
                        .failureHandler((request, response, exception) -> {
                            exception.printStackTrace();
                            String mensagemErro = URLEncoder.encode(
                                    exception.getMessage() != null ? exception.getMessage() : "Falha na autenticacao",
                                    StandardCharsets.UTF_8
                            );
                            response.sendRedirect(frontendUrl + "/#/login?erro=" + mensagemErro);
                        })
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}