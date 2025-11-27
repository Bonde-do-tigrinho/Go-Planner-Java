package com.go.go_planner.application.service;

import com.go.go_planner.application.port.in.UpdateParticipanteRoleUseCase;
import com.go.go_planner.application.port.out.ViagemRepository;
import com.go.go_planner.domain.model.Viagem;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UpdateParticipanteRoleService implements UpdateParticipanteRoleUseCase {

    private final ViagemRepository viagemRepository;

    @Override
    public void execute(UpdateRoleCommand command) {
        Viagem viagem = viagemRepository.findById(command.viagemId())
                .orElseThrow(() -> new NoSuchElementException("Viagem não encontrada."));

        if (!viagem.getCriadorViagemID().equals(command.donoId())) {
            throw new AccessDeniedException("Apenas o dono da viagem pode gerenciar permissões.");
        }

        Viagem.Participante participante = viagem.getParticipantes().stream()
                .filter(p -> p.getUserId().equals(command.participanteId()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Participante não encontrado nesta viagem."));

        participante.setRole(command.novoRole());

        viagemRepository.save(viagem);
    }
}
