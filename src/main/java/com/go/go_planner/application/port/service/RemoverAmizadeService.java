package com.go.go_planner.application.port.service;

import com.go.go_planner.application.port.in.RemoverAmizadeUseCase;
import com.go.go_planner.application.port.out.UsuarioRepositoryPort;
import com.go.go_planner.domain.model.Amigo; // Importe a classe Amigo
import com.go.go_planner.domain.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List; // Importe a classe List

@Service
@RequiredArgsConstructor
public class RemoverAmizadeService implements RemoverAmizadeUseCase {
    private final UsuarioRepositoryPort usuarioRepositoryPort;

    @Override
    public void removerAmizade(RemoverAmizadeCommand command) {
        Usuario usuarioAtual = usuarioRepositoryPort.findById(command.getIdUsuarioAtual())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        Usuario amigoParaRemover = usuarioRepositoryPort.findById(command.getIdAmigoParaRemover())
                .orElseThrow(() -> new RuntimeException("Amigo não encontrado."));

        // 1. Pega a lista de amigos de cada usuário
        List<Amigo> listaAmigosUsuarioAtual = usuarioAtual.getAmigos();
        List<Amigo> listaAmigosDoAmigo = amigoParaRemover.getAmigos();

        // 2. Remove o amigo de cada lista usando a lógica 'removeIf'
        listaAmigosUsuarioAtual.removeIf(amigo -> amigo.getIdAmigo().equals(amigoParaRemover.getId()));
        listaAmigosDoAmigo.removeIf(amigo -> amigo.getIdAmigo().equals(usuarioAtual.getId()));

        // As listas dentro dos objetos 'usuarioAtual' e 'amigoParaRemover'
        // já estão atualizadas. Agora é só salvar.

        // 3. Salva os objetos Usuario atualizados no banco
        usuarioRepositoryPort.save(usuarioAtual);
        usuarioRepositoryPort.save(amigoParaRemover);
    }
}