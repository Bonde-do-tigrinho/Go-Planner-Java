package com.go.go_planner.application.port.in;

public interface RecusarAmizadeUseCase {
    record RecusarAmizadeCommand(String idUsuarioAtual, String idUsuarioRecusado) { }
    void recusarAmizade(RecusarAmizadeCommand recusarAmizadeCommand);
}
