package com.go.go_planner.application.port.in;

public interface ConvidarParticipanteUseCase {
    void convidar(ConvidarCommand command);

    record ConvidarCommand(
            String viagemId,
            String criadorId, // Quem está convidando (dono)
            String emailConvidado
    ) {}
}
