package br.com.juventude.api_contato.ouvidoria;

import br.com.juventude.api_contato.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApoioRepository extends JpaRepository<Apoio, Long> {
    long countByProposta(Proposta proposta);
    boolean existsByUsuarioAndProposta(Usuario usuario, Proposta proposta);
}