package com.go.go_planner.infrastructure.config.adapter.in.web.dto;

public record SolicitarAmizadeRequestDTO (String idUsuarioSolicitado) {

    public String getIdUsuarioSolicitado() {
        return idUsuarioSolicitado;
    }

    public void setIdUsuarioSolicitado(String idUsuarioSolicitado) {
        ;
    }
}