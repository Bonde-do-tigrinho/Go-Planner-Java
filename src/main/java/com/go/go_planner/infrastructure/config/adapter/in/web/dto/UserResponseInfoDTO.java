package com.go.go_planner.infrastructure.config.adapter.in.web.dto;

import com.go.go_planner.domain.model.Notificacao;
import com.go.go_planner.domain.model.StatusUsuario;

import java.util.List;

public record UserResponseInfoDTO(
        String id,
        String nome,
        String email,
        String cpf,
        String foto,
        StatusUsuario status,
        List<String> amigos,
        List<Notificacao> notificacoes,
        List<String> viagensFavoritasIds
) {
}
