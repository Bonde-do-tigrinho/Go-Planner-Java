package com.go.go_planner.infrastructure.config.adapter.in.web.dto;

import com.go.go_planner.domain.model.ViagemRole;
import jakarta.validation.constraints.NotNull;

public record UpdateRoleRequestDTO(
        @NotNull
        ViagemRole role
) {
}
