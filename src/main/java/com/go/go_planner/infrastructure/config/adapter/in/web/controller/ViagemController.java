package com.go.go_planner.infrastructure.config.adapter.in.web.controller;

import com.go.go_planner.application.port.in.*;
import com.go.go_planner.domain.model.Usuario;
import com.go.go_planner.domain.model.Viagem;
import com.go.go_planner.infrastructure.config.adapter.in.web.dto.AtividadeRequestDTO;
import com.go.go_planner.infrastructure.config.adapter.in.web.dto.CreateViagemRequestDTO;
import com.go.go_planner.infrastructure.config.adapter.in.web.dto.UpdateViagemRequestDTO;
import com.go.go_planner.infrastructure.config.adapter.in.web.mapper.ViagemDtoMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
public class ViagemController {

    private final CreateViagemUseCase createViagemUseCase;
    private final GetViagemUseCase getViagemUseCase;
    private final DeleteViagemUseCase deleteViagemUseCase;
    private final UpdateViagemUseCase updateViagemUseCase;
    private final GetMinhasViagensUseCase getMinhasViagensUseCase;
    private final AdicionarAtividadeUseCase adicionarAtividadeUseCase;
    private final DeleteAtividadeUseCase deleteAtividadeUseCase;
    private final ToggleViagemFavoritaUseCase toggleViagemFavoritaUseCase;
    private final GetViagensFavoritasUseCase getViagensFavoritasUseCase;
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

    @PostMapping("/{viagemId}/atividades")
    public ResponseEntity<Viagem> addAtividade(
            @PathVariable String viagemId,
            @Valid @RequestBody AtividadeRequestDTO requestDTO,
            @AuthenticationPrincipal Usuario principal
    ) throws AccessDeniedException {
        AdicionarAtividadeUseCase.AddAtividadeCommand command = new AdicionarAtividadeUseCase.AddAtividadeCommand(
                viagemId, principal.getId(), requestDTO.titulo(), requestDTO.dataHora()
        );

        Viagem viagemAtualizada = adicionarAtividadeUseCase.addAtividade(command);

        return ResponseEntity.status(HttpStatus.CREATED).body(viagemAtualizada);
    }

    @DeleteMapping("/{viagemId}/atividades/{atividadeId}")
    public ResponseEntity<Void> deleteAtividade(
            @PathVariable String viagemId,
            @PathVariable String atividadeId,
            @AuthenticationPrincipal Usuario principal
    ) {
        // Monta o Command
        DeleteAtividadeUseCase.DeleteAtividadeCommand command = new DeleteAtividadeUseCase.DeleteAtividadeCommand(
                viagemId,
                atividadeId,
                principal.getId()
        );

        // Chama o serviço
        deleteAtividadeUseCase.deleteAtividade(command);

        // Retorna 204 No Content (padrão para DELETE bem-sucedido)
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/favorito")
    public ResponseEntity<Map<String, Boolean>> toggleFavorito(
            @PathVariable String id,
            @AuthenticationPrincipal Usuario principal
    ) {
        ToggleViagemFavoritaUseCase.ToggleFavoritoCommand command =
                new ToggleViagemFavoritaUseCase.ToggleFavoritoCommand(id, principal.getId());

        boolean favoritado = toggleViagemFavoritaUseCase.toggleFavorito(command);

        return ResponseEntity.ok(Map.of("favoritado", favoritado));
    }

    @GetMapping("/favoritas")
    public ResponseEntity<List<Viagem>> getMinhasViagensFavoritas(
            @AuthenticationPrincipal Usuario principal
    ) {
        List<Viagem> favoritas = getViagensFavoritasUseCase.execute(principal.getId());

        return ResponseEntity.ok(favoritas);
    }
}