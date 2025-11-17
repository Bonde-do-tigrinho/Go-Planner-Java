package com.go.go_planner.infrastructure.config.adapter.in.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

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

        String descricao,

        String imagem,
        @Valid
        List<AtividadeRequestDTO> atividades,
        List<@Email String> participantes
) {}
