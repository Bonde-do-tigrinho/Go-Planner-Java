package com.go.go_planner.application.service;

import com.go.go_planner.application.port.in.RemoverAmizadeUseCase;
import com.go.go_planner.application.port.out.UsuarioRepository;
import com.go.go_planner.domain.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RemoverAmizadeService implements RemoverAmizadeUseCase {

    @Autowired
    private final UsuarioRepository usuarioRepository;

    @Override
    public void removerAmizade(RemoverAmizadeCommand command) {
        Usuario usuarioAtual = usuarioRepository.findById(command.idUsuarioAtual())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        Usuario amigoParaRemover = usuarioRepository.findById(command.idAmigoRemovido())
                .orElseThrow(() -> new RuntimeException("Amigo não encontrado."));

        // 1. Pega a lista de amigos de cada usuário
        List<String> listaAmigosUsuarioAtual = usuarioAtual.getAmigos();
        List<String> listaAmigosDoAmigo = amigoParaRemover.getAmigos();

        // 2. Remove o amigo de cada lista usando a lógica 'removeIf'
        listaAmigosUsuarioAtual.remove(command.idAmigoRemovido());
        listaAmigosDoAmigo.remove(command.idUsuarioAtual());

        // 3. Salva os objetos Usuario atualizados no banco
        usuarioRepository.save(usuarioAtual);
        usuarioRepository.save(amigoParaRemover);
    }
}