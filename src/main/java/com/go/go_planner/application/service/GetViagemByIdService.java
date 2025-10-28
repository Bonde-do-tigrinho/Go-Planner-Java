package com.go.go_planner.application.service;

import com.go.go_planner.application.port.in.GetViagemUseCase;
import com.go.go_planner.application.port.out.ViagemRepository;
import com.go.go_planner.domain.model.Viagem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class GetViagemByIdService implements GetViagemUseCase {

    private final ViagemRepository viagemRepository;

    @Override
    public Viagem getViagemById(String id) {
        return viagemRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Viagem não encontrada com o ID: " + id));
    }
}