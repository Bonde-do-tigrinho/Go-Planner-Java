package com.go.go_planner.application.port.in;

import com.go.go_planner.domain.model.Viagem;

import java.time.LocalDateTime;

public interface UpdateViagemUseCase {
    Viagem updateViagem(UpdateViagemCommand command);

    record UpdateViagemCommand(
            String viagemId,
            String userId,
            String titulo,
            String localPartida,
            String localDestino,
            LocalDateTime dataPartida,
            LocalDateTime dataRetorno,
            String descricao,
            String imagem,
            String criadorId
    ) {}
}
