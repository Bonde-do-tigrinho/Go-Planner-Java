package com.go.go_planner.application.port.service;

import com.go.go_planner.application.port.in.RemoverAmizadeUseCase;
import com.go.go_planner.application.port.out.UsuarioRepositoryPort;
import com.go.go_planner.domain.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class RemoverAmizadeService implements RemoverAmizadeUseCase {
    private final UsuarioRepositoryPort usuarioRepositoryPort;

    @Override
    public void removerAmizade(RemoverAmizadeCommand command) {
        Usuario usuarioAtual = usuarioRepositoryPort.findById(command.idUsuarioAtual())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        Usuario amigoParaRemover = usuarioRepositoryPort.findById(command.idAmigoRemovido())
                .orElseThrow(() -> new RuntimeException("Amigo não encontrado."));

        // 1. Pega a lista de amigos de cada usuário
        Set<String> listaAmigosUsuarioAtual = usuarioAtual.getAmigos();
        Set<String> listaAmigosDoAmigo = amigoParaRemover.getAmigos();

        // 2. Remove o amigo de cada lista usando a lógica 'removeIf'
        listaAmigosUsuarioAtual.removeIf(amigo -> usuarioAtual.equals(amigoParaRemover));
        listaAmigosDoAmigo.removeIf(amigo -> amigoParaRemover.equals(usuarioAtual));

        // As listas dentro dos objetos 'usuarioAtual' e 'amigoParaRemover'
        // já estão atualizadas. Agora é só salvar.

        // 3. Salva os objetos Usuario atualizados no banco
        usuarioRepositoryPort.save(usuarioAtual);
        usuarioRepositoryPort.save(amigoParaRemover);
    }
}