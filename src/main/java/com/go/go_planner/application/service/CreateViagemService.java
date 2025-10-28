// src/main/java/com/go/go_planner/application/port/service/CreateViagemService.java

package com.go.go_planner.application.port.service;

import com.go.go_planner.application.port.in.CreateViagemUseCase;
import com.go.go_planner.domain.model.Viagem;
import com.go.go_planner.infrastructure.adapter.out.persistence.ViagemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateViagemService implements CreateViagemUseCase {

    private final ViagemRepository viagemRepository;

    @Override
    public Viagem createViagem(Viagem viagem) {
        // Lógica de negócio: validar datas, etc.
        // Delega a persistência ao adaptador (Repository)
        return viagemRepository.save(viagem);
    }
}