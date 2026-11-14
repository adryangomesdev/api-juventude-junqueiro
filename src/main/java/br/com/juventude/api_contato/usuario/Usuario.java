package br.com.juventude.api_contato.usuario;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "provider_id", nullable = false)
    private String providerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProvedorAuthEnum provider;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "foto_url", columnDefinition = "TEXT")
    private String fotoUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleEnum role = RoleEnum.ROLE_JOVEM;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    public Usuario() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getProviderId() { return providerId; }
    public void setProviderId(String providerId) { this.providerId = providerId; }

    public ProvedorAuthEnum getProvider() { return provider; }
    public void setProvider(ProvedorAuthEnum provider) { this.provider = provider; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }

    public RoleEnum getRole() { return role; }
    public void setRole(RoleEnum role) { this.role = role; }

    public LocalDateTime getCriadoEm() { return criadoEm; }
}