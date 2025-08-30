package com.go.go_planner.application.port.service;

import com.go.go_planner.application.port.in.SolicitarAmizadeUseCase;
import com.go.go_planner.application.port.out.UsuarioRepositoryPort;
import com.go.go_planner.domain.model.Amigo;
import com.go.go_planner.domain.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SolicitarAmizadeService implements SolicitarAmizadeUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;

    @Override
    public void solicitarAmizade(SolicitarAmizadeCommand command) {
        Usuario solicitante = usuarioRepositoryPort.findById(command.getIdSolicitante())
                .orElseThrow(() -> new RuntimeException("Usuário solicitante não encontrado: " + command.getIdSolicitante()));

        Usuario solicitado = usuarioRepositoryPort.findById(command.getIdSolicitado())
                .orElseThrow(() -> new RuntimeException("Usuário solicitado não encontrado: " + command.getIdSolicitado()));

        // 1. Cria a representação da amizade pendente para cada usuário
        Amigo amigoParaSolicitante = new Amigo(solicitado.getId(), Amigo.StatusAmizade.PENDENTE);
        Amigo amigoParaSolicitado = new Amigo(solicitante.getId(), Amigo.StatusAmizade.PENDENTE);

        // 2. Adiciona o novo objeto 'Amigo' na lista de cada um
        solicitante.getAmigos().add(amigoParaSolicitante);
        solicitado.getAmigos().add(amigoParaSolicitado);

        // 3. Salva os usuários com as listas atualizadas
        usuarioRepositoryPort.save(solicitante);
        usuarioRepositoryPort.save(solicitado);
    }
}