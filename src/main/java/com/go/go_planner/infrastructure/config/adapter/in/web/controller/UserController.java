package com.go.go_planner.infrastructure.config.adapter.in.web.controller;

import com.go.go_planner.application.port.in.AceitarAmizadeUseCase;
import com.go.go_planner.application.port.in.CreateUserUseCase;
import com.go.go_planner.application.port.in.GetUserUseCase; // Importe o novo UseCase
import com.go.go_planner.application.port.in.RemoverAmizadeUseCase;
import com.go.go_planner.domain.model.Usuario;
import com.go.go_planner.infrastructure.config.adapter.in.web.dto.CreateUserRequestDTO;
import com.go.go_planner.infrastructure.config.adapter.in.web.dto.UserResponseDTO;
import com.go.go_planner.infrastructure.config.adapter.in.web.mapper.UserDtoMapper;
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
    private final GetUserUseCase getUserUseCase; // Adicione o novo UseCase
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

    // Novo endpoint GET
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable String id) {
        try {
            // 1. O controlador chama o Use Case para buscar o usuário
            Usuario usuario = getUserUseCase.getUserById(id);

            // 2. Mapeia o objeto de domínio para o DTO de resposta
            UserResponseDTO response = userDtoMapper.toResponse(usuario);

            // 3. Retorna a resposta HTTP 200 OK com o DTO
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // Se o Use Case lançar uma exceção (usuário não encontrado),
            // retorna uma resposta 404 Not Found.
            return ResponseEntity.notFound().build();
        }
    }
}