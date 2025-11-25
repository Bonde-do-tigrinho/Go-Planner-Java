package com.go.go_planner.infrastructure.config.adapter.in.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ConvidarParticipanteRequestDTO(
        @NotBlank
        @Email
        String email
) {
}
