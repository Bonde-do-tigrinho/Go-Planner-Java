package com.go.go_planner.application.port.in;

public interface AceitarConviteViagemUseCase {
    void execute(AceitarConviteCommand command);

    record AceitarConviteCommand(
            String solicitacaoId,
            String usuarioLogadoId
    ) {}
}