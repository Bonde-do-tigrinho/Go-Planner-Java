package com.go.go_planner.application.port.in;

import com.go.go_planner.domain.model.Viagem;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;

public interface AdicionarAtividadeUseCase {

    Viagem addAtividade(AddAtividadeCommand command) throws AccessDeniedException;

    record AddAtividadeCommand(
            String viagemId,  // O ID da viagem (da URL)
            String userId,    // O ID do usuário (do token)
            String titulo,    // Dados da atividade (do body)
            LocalDateTime dataHora
    ) {}
}
