package com.go.go_planner.application.port.in;

import com.go.go_planner.domain.model.Usuario;

public interface LoginUserUseCase {

    record LoginUserCommand(String email, String senha) {}

    LoginResult loginUser(LoginUserCommand command);

    record LoginResult(String token, Usuario usuario) {}
}