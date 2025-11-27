package com.go.go_planner.application.service;

import com.go.go_planner.application.port.in.RemoverAmizadeUseCase;
import com.go.go_planner.application.port.out.UsuarioRepository;
import com.go.go_planner.domain.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RemoverAmizadeService implements RemoverAmizadeUseCase {

    private final UsuarioRepository usuarioRepository;

    @Override
    public void removerAmizade(RemoverAmizadeCommand command) {
        Usuario usuarioAtual = usuarioRepository.findById(command.idUsuarioAtual())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        Usuario amigoParaRemover = usuarioRepository.findById(command.idAmigoRemovido())
                .orElseThrow(() -> new RuntimeException("Amigo não encontrado."));

        List<String> listaAmigosUsuarioAtual = usuarioAtual.getAmigos();
        List<String> listaAmigosDoAmigo = amigoParaRemover.getAmigos();

        listaAmigosUsuarioAtual.remove(command.idAmigoRemovido());
        listaAmigosDoAmigo.remove(command.idUsuarioAtual());

        usuarioRepository.save(usuarioAtual);
        usuarioRepository.save(amigoParaRemover);
    }
}