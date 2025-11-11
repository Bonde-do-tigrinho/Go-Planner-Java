package com.go.go_planner.infrastructure.config.adapter.in.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequestDTO(
                                       @NotBlank(message = "O nome não pode ser vazio")
                                       String nome,

                                       @NotBlank(message = "O email não pode ser vazio")
                                       @Email(message = "O formato do email é inválido")
                                       String email,

                                       @NotBlank(message = "A senha não pode ser vazia")
                                       @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
                                       String senha,


                                       String cpf
) {
}
