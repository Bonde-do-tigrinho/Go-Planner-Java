package com.go.go_planner.application.port.in;

public interface ToggleViagemFavoritaUseCase {
    boolean toggleFavorito(ToggleFavoritoCommand command);

    record ToggleFavoritoCommand(
            String viagemId,
            String userId
    ) {}
}
