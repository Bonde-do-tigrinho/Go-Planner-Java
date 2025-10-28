package com.go.go_planner.application.service;

import com.go.go_planner.application.port.in.GetViagensFavoritadasUseCase;
import com.go.go_planner.application.port.out.ViagemRepository;
import com.go.go_planner.domain.model.Viagem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetViagensFavoritadasService implements GetViagensFavoritadasUseCase {

    private final ViagemRepository viagemRepository;

    @Override
    public List<Viagem> getViagensFavoritadas() {
        return viagemRepository.findByFavoritadaIsTrue();
    }
}