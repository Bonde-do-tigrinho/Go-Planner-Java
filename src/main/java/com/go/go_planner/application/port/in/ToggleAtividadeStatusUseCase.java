package com.go.go_planner.application.port.in;

import com.go.go_planner.domain.model.Viagem;

public interface ToggleAtividadeStatusUseCase {

    Viagem execute(ToggleAtividadeCommand command);

    record ToggleAtividadeCommand(
            String viagemId,
            String atividadeId,
            String userId
    ) {}
}
