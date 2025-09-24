package com.go.go_planner.application.port.in;


public interface AceitarAmizadeUseCase {

    // 1. Defina o Command aqui dentro como um record (ou classe) público.
    record AceitarAmizadeCommand(String idUsuarioAtual, String idAmigoAprovado) {
    }

    // 2. Altere o método para receber este Command.
    void aceitarAmizade(AceitarAmizadeCommand command);
}