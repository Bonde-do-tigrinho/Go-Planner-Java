package com.go.go_planner.infrastructure.config.adapter.in.web.dto;

import java.time.Instant;

public record ErrorResponseDTO(
        Instant timestamp,
        Integer status,
        String error,
        String message,
        String path
) {
}
