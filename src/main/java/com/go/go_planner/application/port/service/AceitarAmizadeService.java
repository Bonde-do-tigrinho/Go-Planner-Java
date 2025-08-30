package com.go.go_planner.application.port.service;

import com.go.go_planner.application.port.in.AceitarAmizadeUseCase;
import com.go.go_planner.application.port.out.UsuarioRepositoryPort;
import com.go.go_planner.domain.model.Amigo; // Importe a classe Amigo
import com.go.go_planner.domain.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List; // Importe a classe List

@Service
@RequiredArgsConstructor
public class AceitarAmizadeService implements AceitarAmizadeUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;

    @Override
    public void aceitarAmizade(AceitarAmizadeCommand command) {
        Usuario usuarioAtual = usuarioRepositoryPort.findById(command.getIdUsuarioAtual())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        Usuario amigoAprovado = usuarioRepositoryPort.findById(command.getIdAmigoAprovado())
                .orElseThrow(() -> new RuntimeException("Amigo não encontrado."));

        // --- LÓGICA CORRIGIDA AQUI ---

        // 1. Acessa a lista de amigos do usuário atual e atualiza o status do amigo aprovado
        usuarioAtual.getAmigos().stream()
                .filter(amigo -> amigo.getIdAmigo().equals(amigoAprovado.getId()))
                .findFirst()
                .ifPresent(amigo -> amigo.setStatus(Amigo.StatusAmizade.ACEITO));

        // 2. Acessa a lista de amigos do amigo aprovado e atualiza o status do usuário atual
        amigoAprovado.getAmigos().stream()
                .filter(amigo -> amigo.getIdAmigo().equals(usuarioAtual.getId()))
                .findFirst()
                .ifPresent(amigo -> amigo.setStatus(Amigo.StatusAmizade.ACEITO));

        // 3. Salva os objetos Usuario com as listas atualizadas
        usuarioRepositoryPort.save(usuarioAtual);
        usuarioRepositoryPort.save(amigoAprovado);
    }
}