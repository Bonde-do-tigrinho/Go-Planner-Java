package com.go.go_planner.application.port.in;

import com.go.go_planner.domain.model.Viagem;

public interface GetViagemUseCase {
    Viagem getViagemById(String id);
}