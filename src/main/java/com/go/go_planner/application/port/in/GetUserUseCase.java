package com.go.go_planner.application.port.in;

import com.go.go_planner.domain.model.Usuario;

public interface GetUserUseCase {
    Usuario getUserById(String id);
}