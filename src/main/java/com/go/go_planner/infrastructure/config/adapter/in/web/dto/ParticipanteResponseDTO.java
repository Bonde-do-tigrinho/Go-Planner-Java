package com.go.go_planner.infrastructure.config.adapter.in.web.dto;

import com.go.go_planner.domain.model.ViagemRole;

public record ParticipanteResponseDTO(
        String id,
        String nome,
        String email,
        String foto,
        ViagemRole role //
) {
}
