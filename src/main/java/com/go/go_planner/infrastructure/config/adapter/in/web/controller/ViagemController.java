// src/main/java/com/go/go_planner/infrastructure/config/adapter/in/web/controller/ViagemController.java

package com.go.go_planner.infrastructure.config.adapter.in.web.controller;

import com.go.go_planner.application.port.in.GetViagemUseCase;
import com.go.go_planner.application.port.in.GetViagensFavoritadasUseCase;
import com.go.go_planner.domain.model.Viagem;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/viagens") // Mapeamento para viagens
@RequiredArgsConstructor
public class ViagemController {

    private final GetViagemUseCase getViagemUseCase;
    private final GetViagensFavoritadasUseCase getViagensFavoritadasUseCase;

    @GetMapping("/{id}")
    public ResponseEntity<Viagem> getViagemById(@PathVariable String id) {
        try {
            Viagem viagem = getViagemUseCase.getViagemById(id);
            return ResponseEntity.ok(viagem);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/favoritas")
    public ResponseEntity<List<Viagem>> getViagensFavoritas() {
        List<Viagem> viagensFavoritas = getViagensFavoritadasUseCase.getViagensFavoritadas();
        return ResponseEntity.ok(viagensFavoritas);
    }
}