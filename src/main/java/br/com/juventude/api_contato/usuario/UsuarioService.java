package br.com.juventude.api_contato.usuario;

import br.com.juventude.api_contato.auth.strategy.OAuth2UserInfo;
import br.com.juventude.api_contato.exception.RecursoNaoEncontradoException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ApplicationEventPublisher eventPublisher;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          ApplicationEventPublisher eventPublisher) {
        this.usuarioRepository = usuarioRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Usuario salvarOuAtualizarOAuth(OAuth2UserInfo info, ProvedorAuthEnum provider) {
        String emailResolvido = (info.getEmail() != null && !info.getEmail().isBlank())
                ? info.getEmail()
                : provider.name().toLowerCase() + "_" + info.getId() + "@social.local";

        return usuarioRepository.findByProviderAndProviderId(provider, info.getId())
                .or(() -> usuarioRepository.findByEmail(emailResolvido))
                .map(existente -> {
                    existente.setNome(info.getNome());
                    if (info.getFotoUrl() != null && !info.getFotoUrl().isBlank()) {
                        existente.setFotoUrl(info.getFotoUrl());
                    }
                    return usuarioRepository.save(existente);
                })
                .orElseGet(() -> {
                    Usuario novo = new Usuario();
                    novo.setProviderId(info.getId());
                    novo.setProvider(provider);
                    novo.setNome(info.getNome());
                    novo.setEmail(emailResolvido);
                    novo.setFotoUrl(info.getFotoUrl());
                    novo.setRole(RoleEnum.ROLE_JOVEM);
                    return usuarioRepository.save(novo);
                });
    }

    public Usuario buscarPorId(UUID id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado."));
    }

    @Transactional
    public void excluirConta(UUID id) {
        Usuario usuario = buscarPorId(id);
        // Notifica outros módulos (ex: ouvidoria) dentro da mesma transação antes de remover o usuário
        eventPublisher.publishEvent(new UsuarioExcluidoEvent(id));
        usuarioRepository.delete(usuario);
    }
}