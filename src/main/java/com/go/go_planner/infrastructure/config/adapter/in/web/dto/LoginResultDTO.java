package com.go.go_planner.infrastructure.config.adapter.in.web.dto;

import com.go.go_planner.domain.model.Usuario;

public record LoginResultDTO(String token, Usuario usuario) {
}
