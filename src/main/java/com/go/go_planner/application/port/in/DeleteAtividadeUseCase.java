package com.go.go_planner.application.port.in;

public interface DeleteAtividadeUseCase {

    void deleteAtividade(DeleteAtividadeCommand command);

    record DeleteAtividadeCommand(
            String viagemId,
            String atividadeId, // O ID da atividade a ser removida
            String userId       // O ID do usuário (segurança)
    ) {}
}
