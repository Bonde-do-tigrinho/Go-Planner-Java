package com.go.go_planner.application.port.in;

import com.go.go_planner.domain.model.Notificacao;

public interface GetNotificacaoByIdUseCase {
    Notificacao execute(String notificacaoId, String usuarioLogadoId);
}
