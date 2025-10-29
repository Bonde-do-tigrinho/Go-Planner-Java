package com.go.go_planner.infrastructure.config.adapter.in.web.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CreateViagemRequestDTO(
        @NotBlank(message = "O título da viagem é obrigatório.")
        String titulo,

        @NotBlank(message = "O local de partida é obrigatório.")
        String localPartida,

        @NotBlank(message = "O local de destino é obrigatório.")
        String localDestino,

        @NotNull(message = "A data e hora de partida são obrigatórias.")
        @Future(message = "A data de partida deve ser no futuro.")
        LocalDateTime dataPartida,

        @NotNull(message = "A data e hora de retorno são obrigatórias.")
        @Future(message = "A data de retorno deve ser no futuro.")
        LocalDateTime dataRetorno,

        String descricao, // Descrição pode ser opcional

        String imagem // Imagem pode ser opcional
) {}
