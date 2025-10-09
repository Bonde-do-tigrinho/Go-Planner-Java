package com.go.go_planner.application.port.in;

import com.go.go_planner.domain.model.Usuario;

public interface UpdateUserUseCase {


    Usuario updateUser(UpdateUserCommand command);

    record UpdateUserCommand(String userId,
                             String nome,
                             String email,
                             String cpf,
                             String foto) {}
}