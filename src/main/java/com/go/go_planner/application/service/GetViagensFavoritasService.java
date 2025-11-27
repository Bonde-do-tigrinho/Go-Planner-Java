package com.go.go_planner.application.service;

import com.go.go_planner.application.port.in.GetViagensFavoritasUseCase;
import com.go.go_planner.application.port.out.UsuarioRepository;
import com.go.go_planner.application.port.out.ViagemRepository;
import com.go.go_planner.domain.model.Usuario;
import com.go.go_planner.domain.model.Viagem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class GetViagensFavoritasService implements GetViagensFavoritasUseCase {

    private final UsuarioRepository usuarioRepository;
    private final ViagemRepository viagemRepository;

    @Override
    public List<Viagem> execute(String userId) {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado."));

        List<String> idsFavoritos = usuario.getViagensFavoritasIds();

        if (idsFavoritos == null || idsFavoritos.isEmpty()) {
            return Collections.emptyList();
        }

        List<Viagem> viagensFavoritas = viagemRepository.findAllById(idsFavoritos);

        viagensFavoritas.forEach(v -> v.setFavoritada(true));

        return viagensFavoritas;
    }
}
