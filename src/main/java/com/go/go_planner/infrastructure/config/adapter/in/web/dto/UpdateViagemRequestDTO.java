package com.go.go_planner.infrastructure.config.adapter.in.web.dto;

import java.time.LocalDateTime;

public record UpdateViagemRequestDTO(
        String titulo,
        String localPartida,
        String localDestino,
        LocalDateTime dataPartida,
        LocalDateTime dataRetorno,
        String descricao
) {
}
