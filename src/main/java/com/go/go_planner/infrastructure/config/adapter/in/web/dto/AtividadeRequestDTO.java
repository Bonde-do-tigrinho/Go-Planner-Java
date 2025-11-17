package com.go.go_planner.infrastructure.config.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record AtividadeRequestDTO(
        @NotBlank
        String titulo,
        @NotNull
        Date data,
        @NotBlank
        String hora
) {
}
