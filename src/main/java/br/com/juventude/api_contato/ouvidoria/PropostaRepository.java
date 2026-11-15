package br.com.juventude.api_contato.ouvidoria;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PropostaRepository extends JpaRepository<Proposta, Long> {
    List<Proposta> findByStatusOrderByDataCriacaoDesc(StatusProposta status);
    List<Proposta> findAllByOrderByDataCriacaoDesc();

    @Modifying
    @Query("DELETE FROM Proposta p WHERE p.usuario.id = :usuarioId")
    void deleteAllByUsuarioId(@Param("usuarioId") UUID usuarioId);
}