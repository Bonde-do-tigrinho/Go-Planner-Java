package com.go.go_planner.infrastructure.config.adapter.in.web.controller;

// -- Importações existentes --
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

// -- Novas Importações para o POST --
import com.go.go_planner.application.port.in.CreateViagemUseCase;
import com.go.go_planner.infrastructure.config.adapter.in.web.dto.CreateViagemRequestDTO;
import com.go.go_planner.infrastructure.config.adapter.in.web.mapper.ViagemDtoMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import jakarta.validation.Valid;
import java.net.URI;


@RestController
@RequestMapping("/api/viagens") // Mapeamento para viagens
@RequiredArgsConstructor
public class ViagemController {

    // -- Dependências de GET (existentes) --
    private final GetViagemUseCase getViagemUseCase;
    private final GetViagensFavoritadasUseCase getViagensFavoritadasUseCase;

    // -- Novas Dependências para POST (Criação) --
    private final CreateViagemUseCase createViagemUseCase;
    private final ViagemDtoMapper viagemDtoMapper;

    @PostMapping("/createTrip")
    public ResponseEntity<Viagem> createViagem(@Valid @RequestBody CreateViagemRequestDTO request) {
        // 1. Mapeamento do DTO de Requisição para a Entidade de Domínio
        Viagem viagemParaCriar = viagemDtoMapper.toDomain(request);

        // 2. Chamada ao Use Case (Lógica de Negócio)
        Viagem viagemCriada = createViagemUseCase.createViagem(viagemParaCriar);

        // 3. Constrói a URI para a resposta 201 Created (Boas Práticas REST)
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(viagemCriada.getId())
                .toUri();

        // 4. Retorna a resposta 201 Created (Sucesso na Criação)
        return ResponseEntity.created(location).body(viagemCriada);
    }


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