package com.go.go_planner.application.service;

import com.go.go_planner.application.port.in.GetAtividadesUseCase;
import com.go.go_planner.application.port.out.ViagemRepository;
import com.go.go_planner.domain.model.Atividade;
import com.go.go_planner.domain.model.Viagem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAtividadesService implements GetAtividadesUseCase {

    private final ViagemRepository viagemRepository;

    @Override
    public List<Atividade> getAtividades(String viagemId) {
        Viagem viagem = viagemRepository.findById(viagemId)
                .orElseThrow(() -> new IllegalArgumentException("Viagem não encontrada com ID: " + viagemId));

        return viagem.getAtividades();
    }
}