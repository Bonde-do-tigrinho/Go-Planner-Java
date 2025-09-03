package com.go.go_planner.application.port.in;

public interface SolicitarAmizadeUseCase {
    record SolicitarAmizadeCommand(String idUsuarioAtual, String idAmigoSolicitado) {}

    void solicitarAmizade(SolicitarAmizadeCommand command);
}