package com.go.go_planner.application.port.in;


public interface AceitarAmizadeUseCase {

    record AceitarAmizadeCommand(String idUsuarioAtual, String idAmigoAprovado) {
    }
    void aceitarAmizade(AceitarAmizadeCommand command);
}