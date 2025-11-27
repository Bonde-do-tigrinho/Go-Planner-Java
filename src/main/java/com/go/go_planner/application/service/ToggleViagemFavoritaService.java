package com.go.go_planner.application.service;

import com.go.go_planner.application.port.in.ToggleViagemFavoritaUseCase;
import com.go.go_planner.application.port.out.UsuarioRepository;
import com.go.go_planner.application.port.out.ViagemRepository;
import com.go.go_planner.domain.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ToggleViagemFavoritaService implements ToggleViagemFavoritaUseCase {

    private final UsuarioRepository usuarioRepository;
    private final ViagemRepository viagemRepository;

    @Override
    public boolean toggleFavorito(ToggleFavoritoCommand command) {
        Usuario usuario = usuarioRepository.findById(command.userId())
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado."));

        if (!viagemRepository.existsById(command.viagemId())) {
            throw new NoSuchElementException("Viagem não encontrada com ID: " + command.viagemId());
        }

        String idViagem = command.viagemId();
        boolean isFavoritadoAgora;

        if (usuario.getViagensFavoritasIds().contains(idViagem)) {
            usuario.getViagensFavoritasIds().remove(idViagem);
            isFavoritadoAgora = false;
        } else {
            usuario.getViagensFavoritasIds().add(idViagem);
            isFavoritadoAgora = true;
        }

        usuarioRepository.save(usuario);

        return isFavoritadoAgora;
    }
}
