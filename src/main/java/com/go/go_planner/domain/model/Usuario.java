package com.go.go_planner.domain.model;

import com.google.cloud.firestore.annotation.Exclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements UserDetails {

    private String id;
    private String nome;
    private String email;
    private String senha;
    private String cpf;
    private String foto;
    private List<String> amigos;
    private List<Notificacoes> notificacoes;

    // ... seus métodos getAmigos() e getNotificacoes() continuam iguais ...
    public List<String> getAmigos() {
        if (this.amigos == null) { this.amigos = new ArrayList<>(); }
        return this.amigos;
    }
    public List<Notificacoes> getNotificacoes() {
        if (this.notificacoes == null) { this.notificacoes = new ArrayList<>(); }
        return this.notificacoes;
    }


    // --- MÉTODOS DO USERDETAILS CORRIGIDOS ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    @Exclude
    public String getPassword() {
        return this.senha; // CORRETO: Retorna a senha criptografada
    }

    @Override
    public String getUsername() {
        return this.email; // CORRETO: Retorna o email como username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // CORRETO: Sintaxe simplificada
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // CORRETO: Sintaxe simplificada
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // CORRETO: Sintaxe simplificada
    }

    @Override
    public boolean isEnabled() {
        return true; // CORRETO: Sintaxe simplificada
    }
}