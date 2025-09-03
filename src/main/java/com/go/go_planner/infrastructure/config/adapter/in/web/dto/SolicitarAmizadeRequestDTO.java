package com.go.go_planner.infrastructure.config.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;

public record SolicitarAmizadeRequestDTO(
        @NotBlank(message = "O ID do solicitante não pode ser vazio")
        String solicitanteId,

        @NotBlank(message = "O ID do solicitado не pode ser vazio")
        String solicitadoId
) {}