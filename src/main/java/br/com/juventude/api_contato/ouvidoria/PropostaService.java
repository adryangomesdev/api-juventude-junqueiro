package br.com.juventude.api_contato.ouvidoria;

import br.com.juventude.api_contato.exception.ApoioDuplicadoException;
import br.com.juventude.api_contato.exception.PropostaFechadaException;
import br.com.juventude.api_contato.exception.RecursoNaoEncontradoException;
import br.com.juventude.api_contato.ouvidoria.observer.PropostaObserver;
import br.com.juventude.api_contato.usuario.Usuario;
import br.com.juventude.api_contato.usuario.UsuarioExcluidoEvent;
import br.com.juventude.api_contato.usuario.UsuarioService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class PropostaService {

    public static final long META_APOIOS = 20L;

    private final PropostaRepository propostaRepository;
    private final ApoioRepository apoioRepository;
    private final UsuarioService usuarioService;
    private final List<PropostaObserver> observers;

    public PropostaService(PropostaRepository propostaRepository,
                           ApoioRepository apoioRepository,
                           UsuarioService usuarioService,
                           List<PropostaObserver> observers) {
        this.propostaRepository = propostaRepository;
        this.apoioRepository = apoioRepository;
        this.usuarioService = usuarioService;
        this.observers = observers;
    }

    @Transactional
    public PropostaResponseDTO criarProposta(PropostaRequestDTO dto, UUID usuarioId) {
        Usuario autor = usuarioService.buscarPorId(usuarioId);

        Proposta nova = new Proposta();
        nova.setTitulo(dto.titulo());
        nova.setDescricao(dto.descricao());
        nova.setStatus(StatusProposta.EM_VOTACAO);
        nova.setUsuario(autor);

        Proposta salva = propostaRepository.save(nova);
        return PropostaResponseDTO.fromEntity(salva, 0L);
    }

    @Transactional(readOnly = true)
    public List<PropostaResponseDTO> listarTodas() {
        return propostaRepository.findAllByOrderByDataCriacaoDesc().stream()
                .map(p -> PropostaResponseDTO.fromEntity(p, apoioRepository.countByProposta(p)))
                .toList();
    }

    @Transactional
    public PropostaResponseDTO apoiarProposta(UUID usuarioId, Long propostaId) {
        Usuario usuario = usuarioService.buscarPorId(usuarioId);
        Proposta proposta = propostaRepository.findById(propostaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Proposta não encontrada."));

        if (proposta.getStatus() != StatusProposta.EM_VOTACAO) {
            throw new PropostaFechadaException("Esta proposta não está mais aberta para votação.");
        }

        if (apoioRepository.existsByUsuarioAndProposta(usuario, proposta)) {
            throw new ApoioDuplicadoException("Você já apoiou esta proposta anteriormente.");
        }

        apoioRepository.save(new Apoio(usuario, proposta));
        long totalApoios = apoioRepository.countByProposta(proposta);

        // Gatilho automático ao atingir 20 apoios
        if (totalApoios >= META_APOIOS) {
            proposta.setStatus(StatusProposta.EM_ANALISE);
            propostaRepository.save(proposta);

            for (PropostaObserver observer : observers) {
                observer.notificarMetaAtingida(proposta, totalApoios);
            }
        }

        return PropostaResponseDTO.fromEntity(proposta, totalApoios);
    }

    @EventListener
    @Transactional
    public void aoExcluirUsuario(UsuarioExcluidoEvent event) {
        UUID usuarioId = event.usuarioId();
        apoioRepository.deleteAllByUsuarioId(usuarioId);
        apoioRepository.deleteAllByPropostaUsuarioId(usuarioId);
        propostaRepository.deleteAllByUsuarioId(usuarioId);
    }
}