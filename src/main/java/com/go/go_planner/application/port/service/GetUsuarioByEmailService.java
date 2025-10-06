package com.go.go_planner.application.port.service;

import com.go.go_planner.application.port.in.GetUsuarioByEmailUseCase;
import com.go.go_planner.domain.model.Usuario;
import com.go.go_planner.application.port.out.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class GetUsuarioByEmailService implements GetUsuarioByEmailUseCase {

    private final UsuarioRepository usuarioRepository;

    @Override
    public Usuario getUsuarioByEmail(String email) {
        // A lógica de negócio delega a busca ao repositório
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado com o email: " + email));
    }
}