package com.go.go_planner.application.port.in;

import com.go.go_planner.infrastructure.config.adapter.in.web.dto.ParticipanteResponseDTO;

import java.util.List;

public interface GetParticipantesViagemUseCase {
    List<ParticipanteResponseDTO> execute(String viagemId, String usuarioLogadoId);
}
