package br.com.juventude.api_contato.usuario;

import br.com.juventude.api_contato.auth.strategy.OAuth2UserInfo;
import br.com.juventude.api_contato.exception.RecursoNaoEncontradoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Usuario salvarOuAtualizarOAuth(OAuth2UserInfo info, ProvedorAuthEnum provider) {
        return usuarioRepository.findByEmail(info.getEmail())
                .map(existente -> {
                    existente.setNome(info.getNome());
                    existente.setFotoUrl(info.getFotoUrl());
                    return usuarioRepository.save(existente);
                })
                .orElseGet(() -> {
                    Usuario novo = new Usuario();
                    novo.setProviderId(info.getId());
                    novo.setProvider(provider);
                    novo.setNome(info.getNome());
                    novo.setEmail(info.getEmail());
                    novo.setFotoUrl(info.getFotoUrl());
                    novo.setRole(RoleEnum.ROLE_JOVEM);
                    return usuarioRepository.save(novo);
                });
    }

    public Usuario buscarPorId(java.util.UUID id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado."));
    }

    @Transactional
    public void excluirConta(java.util.UUID id) {
        Usuario usuario = buscarPorId(id);
        usuarioRepository.delete(usuario);
    }
}