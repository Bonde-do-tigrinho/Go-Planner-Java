package com.go.go_planner.infrastructure.config.adapter.in.web.dto;

public record UpdateUserRequestDTO(
        String id,
        String nome,
        String email,
        String senhaAtual,
        String senhaNova){
}
