package com.go.go_planner.infrastructure.config.adapter.in.web.controller;

import com.go.go_planner.application.port.in.*;
import com.go.go_planner.domain.model.Usuario;
import com.go.go_planner.domain.model.Viagem;
import com.go.go_planner.infrastructure.config.adapter.in.web.dto.CreateViagemRequestDTO;
import com.go.go_planner.infrastructure.config.adapter.in.web.dto.UpdateViagemRequestDTO;
import com.go.go_planner.infrastructure.config.adapter.in.web.mapper.ViagemDtoMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
public class ViagemController {

    private final CreateViagemUseCase createViagemUseCase;
    private final GetViagemUseCase getViagemUseCase;
    private final GetViagensFavoritadasUseCase getViagensFavoritadasUseCase;
    private final DeleteViagemUseCase deleteViagemUseCase;
    private final UpdateViagemUseCase updateViagemUseCase;
    private final GetMinhasViagensUseCase getMinhasViagensUseCase;
    private final ViagemDtoMapper viagemDtoMapper;

    @GetMapping("/{id}")
    public ResponseEntity<Viagem> getViagemById(@PathVariable String id) {
        Viagem viagem = getViagemUseCase.getViagemById(id);
        return ResponseEntity.ok(viagem);
    }

    @PostMapping("create-trip")
    public ResponseEntity<Viagem> createViagem(
            @Valid @RequestBody CreateViagemRequestDTO request,
            @AuthenticationPrincipal Usuario principal
    ) {
        if (principal == null) {
            throw new IllegalStateException("Principal não pode ser nulo. Verifique o filtro de autenticação.");
        }

        String criadorId = principal.getId();
        CreateViagemUseCase.CreateViagemCommand command = viagemDtoMapper.toCommand(request, criadorId);
        Viagem viagemCriada = createViagemUseCase.createViagem(command);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(viagemCriada.getId())
                .toUri();

        return ResponseEntity.created(location).body(viagemCriada);
    }

    @GetMapping("/favoritas")
    public ResponseEntity<List<Viagem>> getViagensFavoritas() {
        List<Viagem> viagensFavoritas = getViagensFavoritadasUseCase.getViagensFavoritadas();
        return ResponseEntity.ok(viagensFavoritas);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteViagem(
            @PathVariable String id,
            @AuthenticationPrincipal Usuario principal) {
        deleteViagemUseCase.deleteViagem(id, principal.getId());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Viagem> updateViagem(
            @PathVariable String id,
            @RequestBody UpdateViagemRequestDTO request,
            @AuthenticationPrincipal Usuario principal
    ) {
        UpdateViagemUseCase.UpdateViagemCommand command =
                viagemDtoMapper.toUpdateCommand(request, id, principal.getId());

        Viagem viagemAtualizada = updateViagemUseCase.updateViagem(command);

        return ResponseEntity.ok(viagemAtualizada);
    }

    @GetMapping("/minhas-viagens")
    public ResponseEntity<List<Viagem>> getMinhasViagens(@AuthenticationPrincipal Usuario principal) {

        List<Viagem> viagens = getMinhasViagensUseCase.getMinhasViagens(principal.getId());

        return ResponseEntity.ok(viagens);
    }
}