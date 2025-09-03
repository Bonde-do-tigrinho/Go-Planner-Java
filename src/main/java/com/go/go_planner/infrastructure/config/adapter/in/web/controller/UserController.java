package com.go.go_planner.infrastructure.config.adapter.in.web.controller;

import com.go.go_planner.application.port.in.AceitarAmizadeUseCase;
import com.go.go_planner.application.port.in.CreateUserUseCase;
import com.go.go_planner.application.port.in.RemoverAmizadeUseCase;
import com.go.go_planner.domain.model.Usuario;
import com.go.go_planner.infrastructure.config.adapter.in.web.dto.CreateUserRequestDTO;
import com.go.go_planner.infrastructure.config.adapter.in.web.dto.UserResponseDTO;
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


    @PostMapping("/cadastrar")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody CreateUserRequestDTO request) {
        Usuario usuarioParaCriar = userDtoMapper.toDomain(request);

        Usuario usuarioCriado = createUserUseCase.createUser(usuarioParaCriar);

        UserResponseDTO response = userDtoMapper.toResponse(usuarioCriado);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(usuarioCriado.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);

    }

}
