package com.go.go_planner.application.service;

import com.go.go_planner.application.port.in.LoginUserUseCase;
import com.go.go_planner.application.port.out.UsuarioRepository;
import com.go.go_planner.domain.model.Usuario;
import com.go.go_planner.infrastructure.config.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginUserService implements LoginUserUseCase {
    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;

    @Override
    public LoginResult loginUser(LoginUserCommand command) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(command.email(), command.senha())
        );
        Usuario usuario = usuarioRepository.findByEmail(command.email())
                .orElseThrow(() -> new IllegalStateException("Usuário autenticado não encontrado."));
        String token = jwtService.generateToken(usuario);
        return new LoginResult(token, usuario);
    }
}