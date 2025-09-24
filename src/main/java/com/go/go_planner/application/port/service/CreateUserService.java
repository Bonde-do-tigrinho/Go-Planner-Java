package com.go.go_planner.application.port.service;

import com.go.go_planner.application.port.in.CreateUserUseCase;
import com.go.go_planner.application.port.out.UsuarioRepositoryPort;
import com.go.go_planner.domain.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateUserService implements CreateUserUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Usuario createUser(Usuario usuario) {
        if (usuarioRepositoryPort.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("Email já está em uso.");
        }

        String encodedPassword = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(encodedPassword);

        return usuarioRepositoryPort.save(usuario);
    }

}