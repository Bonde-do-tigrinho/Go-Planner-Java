// src/main/java/com/go/go_planner/infrastructure/config/adapter/in/web/dto/CreateViagemRequestDTO.java

package com.go.go_planner.infrastructure.config.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateViagemRequestDTO {

    @NotBlank(message = "O destino é obrigatório.")
    private String destino;

    @NotNull(message = "A data de início é obrigatória.")
    private LocalDate dataInicio;

    @NotNull(message = "A data de fim é obrigatória.")
    private LocalDate dataFim;

    // Outros campos relevantes para a criação da viagem (ex: userId)
}