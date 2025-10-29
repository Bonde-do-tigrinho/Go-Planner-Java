package com.go.go_planner.application.port.in;

import com.go.go_planner.domain.model.Viagem;
import java.time.LocalDateTime;

public interface CreateViagemUseCase {

    Viagem createViagem(CreateViagemCommand command);

    record CreateViagemCommand(
            String titulo,
            String localPartida,
            String localDestino,
            LocalDateTime dataPartida,
            LocalDateTime dataRetorno,
            String descricao,
            String imagem,
            String criadorId // O ID do usuário autenticado
    ) {}
}