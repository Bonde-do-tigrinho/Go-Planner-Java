package com.go.go_planner.application.port.in;

import com.go.go_planner.domain.model.Notificacao;

import java.util.List;

public interface GetMinhasNotificacoesUseCase {
    List<Notificacao> execute(String userId);
}
