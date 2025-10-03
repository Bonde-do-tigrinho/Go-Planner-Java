package com.go.go_planner.domain.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Document(collection = "usuarios")
public class Usuario implements UserDetails {

    @Id
    private String id;
    private String nome;
    private String email;
    private String senha;
    private String cpf;
    private String foto;
    private List<String> amigos = new ArrayList<>();
    private List<Notificacao> notificacoes = new ArrayList<>();

    // ... seus métodos getAmigos() e getNotificacoes() continuam iguais ...
    public List<String> getAmigos() {
        if (this.amigos == null) { this.amigos = new ArrayList<>(); }
        return this.amigos;
    }
    public List<Notificacao> getNotificacoes() {
        if (this.notificacoes == null) { this.notificacoes = new ArrayList<>(); }
        return this.notificacoes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email; // Deve estar assim
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}