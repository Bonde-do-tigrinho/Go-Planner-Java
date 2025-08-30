package com.go.go_planner.infrastructure.config.adapter.in.web.controller;

import com.go.go_planner.application.port.in.AceitarAmizadeUseCase;
import com.go.go_planner.application.port.in.CreateUserUseCase;
import com.go.go_planner.application.port.in.RemoverAmizadeUseCase;
import com.go.go_planner.domain.model.Usuario;
import com.go.go_planner.infrastructure.config.adapter.in.web.dto.CreateUserRequestDTO;
import com.go.go_planner.infrastructure.config.adapter.in.web.mapper.UserDtoMapper;
import com.google.firebase.remoteconfig.internal.TemplateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final UserDtoMapper userDtoMapper;
    private final AceitarAmizadeUseCase aceitarAmizadeUseCase;
    private final RemoverAmizadeUseCase removerAmizadeUseCase;

    @PostMapping("/cadastrar")
    public ResponseEntity<TemplateResponse.UserResponse> createUser(@Valid @RequestBody CreateUserRequestDTO request) {
        Usuario usuarioParaCriar = userDtoMapper.toDomain(request);

        Usuario usuarioCriado = createUserUseCase.createUser(usuarioParaCriar);

        TemplateResponse.UserResponse response = userDtoMapper.toResponse(usuarioCriado);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(usuarioCriado.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);


    }
    @PatchMapping("/{idUsuario}/accept-friend/{idAmigo}")
    public ResponseEntity<Void> aceitarAmizade(@PathVariable String idUsuario, @PathVariable String idAmigo) {
        var command = new AceitarAmizadeUseCase.AceitarAmizadeCommand(idUsuario, idAmigo);
        aceitarAmizadeUseCase.aceitarAmizade();
        return ResponseEntity.ok().build();
    }
    // ... Código idêntico ao anterior ...
    @DeleteMapping("/{idUsuario}/remove-friend/{idAmigo}")
    public ResponseEntity<Void> removerAmizade(@PathVariable String idUsuario, @PathVariable String idAmigo) {
        var command = new RemoverAmizadeUseCase. RemoverAmizadeCommand(idUsuario, idAmigo);

        // CORREÇÃO AQUI: O nome do método deve começar com letra minúscula.
        removerAmizadeUseCase.removerAmizade();

        return ResponseEntity.noContent().build();
    }

    private class RemoverAmizadeCommand {
        public RemoverAmizadeCommand(String idUsuario, String idAmigo) {
        }
    }
}
