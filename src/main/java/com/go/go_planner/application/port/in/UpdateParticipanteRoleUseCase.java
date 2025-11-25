package com.go.go_planner.application.port.in;

import com.go.go_planner.domain.model.ViagemRole;

public interface UpdateParticipanteRoleUseCase {
    void execute(UpdateRoleCommand command);

    record UpdateRoleCommand(
            String viagemId,
            String donoId,        // Quem está fazendo a alteração (tem que ser o dono)
            String participanteId, // Quem vai sofrer a alteração
            ViagemRole novoRole    // O novo papel (EDITOR ou LEITOR)
    ) {}
}
