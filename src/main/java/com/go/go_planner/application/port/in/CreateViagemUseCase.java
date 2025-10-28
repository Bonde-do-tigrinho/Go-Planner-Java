// src/main/java/com/go/go_planner/application/port/in/CreateViagemUseCase.java

package com.go.go_planner.application.port.in;

import com.go.go_planner.domain.model.Viagem;

public interface CreateViagemUseCase {
    Viagem createViagem(Viagem viagem);
}