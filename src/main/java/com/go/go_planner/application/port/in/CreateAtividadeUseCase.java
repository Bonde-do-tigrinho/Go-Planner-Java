package com.go.go_planner.application.port.in;

import java.time.LocalDateTime;

public interface CreateAtividadeUseCase {

    String createAtividade(CreateAtividadeCommand command);
    record CreateAtividadeCommand(
            String viagemId,
            String titulo,
            LocalDateTime dataAtividade,
            Boolean concluida
    ) {}
}