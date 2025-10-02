package com.go.go_planner.application.port.in;

import com.go.go_planner.domain.model.Usuario;

public interface LoginUserUseCase {
    LoginResult loginUser(LoginUserCommand command);
    record LoginUserCommand(String email, String senha) {}
    record LoginResult(String token, Usuario usuario) {}
}