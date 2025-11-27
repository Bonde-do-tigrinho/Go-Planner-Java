package com.go.go_planner.application.service;

import com.go.go_planner.application.port.in.GetViagensParticipandoUseCase;
import com.go.go_planner.application.port.out.ViagemRepository;
import com.go.go_planner.domain.model.Viagem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetViagensParticipandoService implements GetViagensParticipandoUseCase {
    private final ViagemRepository viagemRepository;

    @Override
    public List<Viagem> execute(String userId) {
        return viagemRepository.findByParticipantesUserId(userId);
    }
}
