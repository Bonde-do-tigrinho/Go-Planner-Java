package com.go.go_planner.application.port.service;

import com.go.go_planner.application.port.in.GetUserUseCase;
import com.go.go_planner.application.port.out.UsuarioRepositoryPort;
import com.go.go_planner.domain.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements GetUserUseCase {

    private final UsuarioRepositoryPort userRepository; // O repositório é a porta de saída

    @Override
    public Usuario getUserById(String id) {
        // Implemente a lógica de busca aqui.
        // O repositório irá fazer a comunicação com o banco de dados.
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
    }
}