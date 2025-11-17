package com.go.go_planner.application.service;

import com.go.go_planner.application.port.in.GetMinhasViagensUseCase;
import com.go.go_planner.application.port.out.ViagemRepository;
import com.go.go_planner.domain.model.Viagem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetMinhasViagensService implements GetMinhasViagensUseCase {

    private final ViagemRepository viagemRepository;

    @Override
    public List<Viagem> getMinhasViagens(String userId) {
        // A lógica é simplesmente chamar o método do repositório
        return viagemRepository.findByCriadorViagemID(userId);
    }
}
