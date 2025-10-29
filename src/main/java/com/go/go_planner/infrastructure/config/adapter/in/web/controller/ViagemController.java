package com.go.go_planner.infrastructure.config.adapter.in.web.controller;

import com.go.go_planner.application.port.in.CreateViagemUseCase;
import com.go.go_planner.application.port.in.GetViagemUseCase;
import com.go.go_planner.application.port.in.GetViagensFavoritadasUseCase;
import com.go.go_planner.domain.model.Usuario;
import com.go.go_planner.domain.model.Viagem;
import com.go.go_planner.infrastructure.config.adapter.in.web.dto.CreateViagemRequestDTO;
import com.go.go_planner.infrastructure.config.adapter.in.web.mapper.ViagemDtoMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.jwt.Jwt;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/viagens")
@RequiredArgsConstructor
public class ViagemController {

    private final CreateViagemUseCase createViagemUseCase;
    private final GetViagemUseCase getViagemUseCase;
    private final GetViagensFavoritadasUseCase getViagensFavoritadasUseCase;
    private final ViagemDtoMapper viagemDtoMapper;

    @GetMapping("/{id}")
    public ResponseEntity<Viagem> getViagemById(@PathVariable String id) {
        Viagem viagem = getViagemUseCase.getViagemById(id);
        return ResponseEntity.ok(viagem);
    }

    @PostMapping
    public ResponseEntity<Viagem> createViagem(
            @Valid @RequestBody CreateViagemRequestDTO request,
            // AQUI ESTÁ A CORREÇÃO: Troque o tipo para org.springframework.security.oauth2.jwt.Jwt
            @AuthenticationPrincipal Usuario principal
    ) {
        if (principal == null) {
            throw new IllegalStateException("Principal não pode ser nulo. Verifique o filtro de autenticação.");
        }

        String criadorId = principal.getId(); // ou principal.getUsername() se o ID for o email, etc.
        // O resto do seu código já está correto
        CreateViagemUseCase.CreateViagemCommand command = viagemDtoMapper.toCommand(request, criadorId);
        Viagem viagemCriada = createViagemUseCase.createViagem(command);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(viagemCriada.getId())
                .toUri();

        return ResponseEntity.created(location).body(viagemCriada);
    }

    /**
     * Retorna uma lista de todas as viagens favoritadas.
     * @return Resposta 200 OK com a lista de viagens.
     */
    @GetMapping("/favoritas")
    public ResponseEntity<List<Viagem>> getViagensFavoritas() {
        List<Viagem> viagensFavoritas = getViagensFavoritadasUseCase.getViagensFavoritadas();
        return ResponseEntity.ok(viagensFavoritas);
    }
}