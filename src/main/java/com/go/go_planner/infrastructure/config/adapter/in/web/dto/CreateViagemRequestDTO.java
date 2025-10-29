// src/main/java/com/go/go_planner/infrastructure/config/adapter/in/web/dto/CreateViagemRequestDTO.java

package com.go.go_planner.infrastructure.config.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateViagemRequestDTO {

    @NotBlank(message = "O título da viagem é obrigatório.")
    private String titulo;

    @NotBlank(message = "O local de partida é obrigatório.")
    private String localPartida;

    @NotBlank(message = "O local de destino é obrigatório.")
    private String localDestino;

    @NotNull(message = "A data e hora de partida são obrigatórias.")
    private LocalDateTime dataPartida;

    @NotNull(message = "A data e hora de retorno são obrigatórias.")
    private LocalDateTime dataRetorno;

    @NotBlank(message = "A descrição é obrigatória.")
    private String descricao;

    @NotBlank(message = "O ID do criador da viagem é obrigatório.")
    private String criadorViagemId;

}