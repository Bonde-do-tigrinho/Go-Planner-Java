package com.go.go_planner.application.port.in;


public interface AceitarAmizadeUseCase {

    record AceitarAmizadeCommand(String idUsuarioAtual, String idAmigoAprovado) {
    }

    // 2. Altere o método para receber este RecusarAmizadeCommand.
    void aceitarAmizade(AceitarAmizadeCommand command);
}