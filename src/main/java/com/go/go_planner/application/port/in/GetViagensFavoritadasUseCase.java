package com.go.go_planner.application.port.in;

import com.go.go_planner.domain.model.Viagem;
import java.util.List;

public interface GetViagensFavoritadasUseCase {
    List<Viagem> getViagensFavoritadas();
}