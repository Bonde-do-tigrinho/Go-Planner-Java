package com.go.go_planner.application.port.in;

public interface RemoverAmizadeUseCase {

    record RemoverAmizadeCommand(String idUsuarioAtual, String idAmigoRemovido) {
    }

    void removerAmizade(RemoverAmizadeCommand command);
}