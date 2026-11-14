package br.com.juventude.api_contato.ouvidoria;

import br.com.juventude.api_contato.exception.ApoioDuplicadoException;
import br.com.juventude.api_contato.exception.PropostaFechadaException;
import br.com.juventude.api_contato.ouvidoria.observer.PropostaObserver;
import br.com.juventude.api_contato.usuario.Usuario;
import br.com.juventude.api_contato.usuario.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PropostaServiceTest {

    @Mock
    private PropostaRepository propostaRepository;

    @Mock
    private ApoioRepository apoioRepository;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private PropostaObserver observer;

    private PropostaService propostaService;

    @BeforeEach
    void setUp() {
        propostaService = new PropostaService(
                propostaRepository,
                apoioRepository,
                usuarioService,
                List.of(observer)
        );
    }

    @Test
    void deveAlterarStatusParaEmAnaliseENotificarObserverAoAtingir20Apoios() {
        UUID usuarioId = UUID.randomUUID();
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        usuario.setNome("Jovem Teste");

        Proposta proposta = new Proposta();
        proposta.setId(1L);
        proposta.setTitulo("Quadra Poliesportiva");
        proposta.setStatus(StatusProposta.EM_VOTACAO);
        proposta.setUsuario(usuario);

        when(usuarioService.buscarPorId(usuarioId)).thenReturn(usuario);
        when(propostaRepository.findById(1L)).thenReturn(Optional.of(proposta));
        when(apoioRepository.existsByUsuarioAndProposta(usuario, proposta)).thenReturn(false);
        when(apoioRepository.countByProposta(proposta)).thenReturn(20L);

        PropostaResponseDTO resultado = propostaService.apoiarProposta(usuarioId, 1L);

        assertEquals(StatusProposta.EM_ANALISE, resultado.status());
        assertEquals(20L, resultado.totalApoios());
        verify(propostaRepository, times(1)).save(proposta);
        verify(observer, times(1)).notificarMetaAtingida(proposta, 20L);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioTentarApoiarDuasVezes() {
        UUID usuarioId = UUID.randomUUID();
        Usuario usuario = new Usuario();
        Proposta proposta = new Proposta();
        proposta.setStatus(StatusProposta.EM_VOTACAO);

        when(usuarioService.buscarPorId(usuarioId)).thenReturn(usuario);
        when(propostaRepository.findById(1L)).thenReturn(Optional.of(proposta));
        when(apoioRepository.existsByUsuarioAndProposta(usuario, proposta)).thenReturn(true);

        assertThrows(ApoioDuplicadoException.class, () -> propostaService.apoiarProposta(usuarioId, 1L));
        verify(apoioRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoPropostaNaoEstiverEmVotacao() {
        UUID usuarioId = UUID.randomUUID();
        Usuario usuario = new Usuario();
        Proposta proposta = new Proposta();
        proposta.setStatus(StatusProposta.EM_ANALISE);

        when(usuarioService.buscarPorId(usuarioId)).thenReturn(usuario);
        when(propostaRepository.findById(1L)).thenReturn(Optional.of(proposta));

        assertThrows(PropostaFechadaException.class, () -> propostaService.apoiarProposta(usuarioId, 1L));
    }
}