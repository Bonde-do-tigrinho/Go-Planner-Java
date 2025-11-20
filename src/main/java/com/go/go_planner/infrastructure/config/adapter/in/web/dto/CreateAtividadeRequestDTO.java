package com.go.go_planner.infrastructure.config.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CreateAtividadeRequestDTO(
        @NotBlank String id,
        @NotBlank String titulo,
        @NotNull LocalDateTime data,
        Boolean concluida

) {}