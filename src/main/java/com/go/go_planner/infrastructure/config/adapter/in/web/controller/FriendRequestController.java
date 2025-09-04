package com.go.go_planner.infrastructure.config.adapter.in.web.controller;

import com.go.go_planner.application.port.in.AceitarAmizadeUseCase;
import com.go.go_planner.application.port.in.RemoverAmizadeUseCase;
import com.go.go_planner.application.port.in.SolicitarAmizadeUseCase;
import com.go.go_planner.infrastructure.config.adapter.in.web.dto.SolicitarAmizadeRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/friend")
@RequiredArgsConstructor
public class FriendRequestController {


    private final SolicitarAmizadeUseCase solicitarAmizadeUseCase;
    private final AceitarAmizadeUseCase aceitarAmizadeUseCase;
    private final RemoverAmizadeUseCase removerAmizadeUseCase;

    @PostMapping("/request")
    public ResponseEntity<Void> solicitarAmizade(@Valid @RequestBody SolicitarAmizadeRequestDTO request) {
        var command = new SolicitarAmizadeUseCase.SolicitarAmizadeCommand(
                request.solicitanteId(),
                request.solicitadoId()
        );
        solicitarAmizadeUseCase.solicitarAmizade(command);

        return ResponseEntity.accepted().build();
    }

    @PatchMapping("/accept-friend")
    public ResponseEntity<String> aceitarAmizade(@Valid @RequestBody SolicitarAmizadeRequestDTO request) {
        var command = new AceitarAmizadeUseCase.AceitarAmizadeCommand(request.solicitadoId(), request.solicitanteId());
        aceitarAmizadeUseCase.aceitarAmizade(command);
        return ResponseEntity.ok().body("Amizade aceita com sucesso!");
    }
    // ... Código idêntico ao anterior ...
    @DeleteMapping("/{idUsuario}/remove-friend/{idAmigo}")
    public ResponseEntity<String> removerAmizade(@PathVariable String idUsuario, @PathVariable String idAmigo) {
        var command = new RemoverAmizadeUseCase.RemoverAmizadeCommand(idUsuario, idAmigo);
        removerAmizadeUseCase.removerAmizade(command);
        return ResponseEntity.ok().body("Amizade removida com sucesso!");
    }
}
