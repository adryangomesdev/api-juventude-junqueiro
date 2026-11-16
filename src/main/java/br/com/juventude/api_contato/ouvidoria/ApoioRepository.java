package br.com.juventude.api_contato.ouvidoria;

import br.com.juventude.api_contato.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ApoioRepository extends JpaRepository<Apoio, Long> {
    long countByProposta(Proposta proposta);
    boolean existsByUsuarioAndProposta(Usuario usuario, Proposta proposta);

    @Modifying
    @Query("DELETE FROM Apoio a WHERE a.usuario.id = :usuarioId")
    void deleteAllByUsuarioId(@Param("usuarioId") UUID usuarioId);

    @Modifying
    @Query("DELETE FROM Apoio a WHERE a.proposta.usuario.id = :usuarioId")
    void deleteAllByPropostaUsuarioId(@Param("usuarioId") UUID usuarioId);

    @Modifying
    @Query("DELETE FROM Apoio a WHERE a.proposta.id = :propostaId")
    void deleteAllByPropostaId(@Param("propostaId") Long propostaId);
}