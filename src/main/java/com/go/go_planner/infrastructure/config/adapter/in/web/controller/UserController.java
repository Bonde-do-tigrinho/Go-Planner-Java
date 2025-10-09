package com.go.go_planner.infrastructure.config.adapter.in.web.controller;

import com.go.go_planner.application.port.in.CreateUserUseCase;
import com.go.go_planner.application.port.in.GetUserUseCase;
import com.go.go_planner.application.port.in.LoginUserUseCase;
import com.go.go_planner.application.port.in.UpdateUserUseCase;
import com.go.go_planner.domain.model.Usuario;
import com.go.go_planner.infrastructure.config.adapter.in.web.dto.*;
import com.go.go_planner.infrastructure.config.adapter.in.web.mapper.UserDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final GetUserUseCase getUserUseCase;
    private final UserDtoMapper userDtoMapper;
    private final LoginUserUseCase loginUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;


    @PostMapping("/cadastrar")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody CreateUserRequestDTO request) {
        try {
            Usuario usuarioParaCriar = userDtoMapper.toDomain(request);
            Usuario usuarioCriado = createUserUseCase.createUser(usuarioParaCriar);
            UserResponseDTO response = userDtoMapper.toResponse(usuarioCriado);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(usuarioCriado.getId())
                    .toUri();
            return ResponseEntity.created(location).body(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginUser(@Valid @RequestBody LoginRequestDTO request) {
        var command = userDtoMapper.toLoginCommand(request);
        LoginUserUseCase.LoginResult result = loginUserUseCase.loginUser(command);
        UserResponseDTO userInfo = userDtoMapper.toResponse(result.usuario());
        LoginResponseDTO response = new LoginResponseDTO(result.token(), userInfo);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable String id) {
        try {
            Usuario usuarioEncontrado = getUserUseCase.getUserById(id);
            UserResponseDTO response = userDtoMapper.toResponse(usuarioEncontrado);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/me")
    public ResponseEntity<UserResponseDTO> updateUser(
            @Valid @RequestBody UpdateUserRequestDTO request,
            @AuthenticationPrincipal Usuario usuarioLogado
    ) {

        var command = new UpdateUserUseCase.UpdateUserCommand(
                usuarioLogado.getId(),
                request.nome(),
                request.email(),
                request.cpf(),
                request.foto()
        );

        Usuario usuarioAtualizado = updateUserUseCase.updateUser(command);

        UserResponseDTO response = userDtoMapper.toResponse(usuarioAtualizado);

        return ResponseEntity.ok(response);
    }

}