package com.go.go_planner.application.port.in;

public interface RecusarConviteViagemUseCase {

    void execute(RecusarConviteCommand command);

    record RecusarConviteCommand(
            String solicitacaoId,
            String usuarioLogadoId
    ) {}
}
