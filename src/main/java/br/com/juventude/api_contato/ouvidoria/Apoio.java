package br.com.juventude.api_contato.ouvidoria;

import br.com.juventude.api_contato.usuario.Usuario;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "apoios",
        uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "proposta_id"})
)
public class Apoio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_apoio", nullable = false, updatable = false)
    private LocalDateTime dataApoio = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposta_id", nullable = false)
    private Proposta proposta;

    public Apoio() {}

    public Apoio(Usuario usuario, Proposta proposta) {
        this.usuario = usuario;
        this.proposta = proposta;
    }

    public Long getId() { return id; }
    public LocalDateTime getDataApoio() { return dataApoio; }
    public Usuario getUsuario() { return usuario; }
    public Proposta getProposta() { return proposta; }
}