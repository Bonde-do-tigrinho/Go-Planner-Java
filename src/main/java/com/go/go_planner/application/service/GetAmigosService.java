package com.go.go_planner.application.service;

import com.go.go_planner.application.port.in.GetAmigosUseCase;
import com.go.go_planner.application.port.out.UsuarioRepository;
import com.go.go_planner.domain.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class GetAmigosService implements GetAmigosUseCase {

    private final UsuarioRepository usuarioRepository;

    @Override
    public List<Usuario> getAmigos(String userId) {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado."));

        if (usuario.getAmigos() == null || usuario.getAmigos().isEmpty()) {
            return Collections.emptyList();
        }

        return usuarioRepository.findByIdIn(usuario.getAmigos());
    }
}