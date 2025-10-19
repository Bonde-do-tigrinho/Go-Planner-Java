package com.go.go_planner.infrastructure.config.adapter.in.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ConfirmAccountRequestDTO(

        @NotBlank(message = "O email não pode ser vazio.")
        @Email(message = "O formato do email é inválido.")
        String email,

        @NotBlank(message = "O código não pode ser vazio.")
        @Size(min = 6, max = 6, message = "O código deve ter exatamente 6 dígitos.")
        String codigo
) {}