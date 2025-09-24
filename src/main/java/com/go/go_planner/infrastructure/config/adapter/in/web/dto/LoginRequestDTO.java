package com.go.go_planner.infrastructure.config.adapter.in.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank(message = "O email não pode ser vazio")
        @Email
        String email,
        @NotBlank(message = "A senha não pode ser vazia")
        String senha) {
}
