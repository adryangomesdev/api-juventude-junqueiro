package br.com.juventude.api_contato.ouvidoria;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropostaRepository extends JpaRepository<Proposta, Long> {
    List<Proposta> findByStatusOrderByDataCriacaoDesc(StatusProposta status);
    List<Proposta> findAllByOrderByDataCriacaoDesc();
}