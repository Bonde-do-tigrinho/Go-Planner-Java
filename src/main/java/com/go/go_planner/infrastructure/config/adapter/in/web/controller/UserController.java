package com.go.go_planner.infrastructure.config.adapter.in.web.controller;

import com.go.go_planner.application.port.in.*;
import com.go.go_planner.domain.model.Usuario;
import com.go.go_planner.infrastructure.config.adapter.in.web.dto.*;
import com.go.go_planner.infrastructure.config.adapter.in.web.mapper.UserDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.go.go_planner.application.port.in.GetUsuarioByEmailUseCase;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.NoSuchElementException;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final GetUserUseCase getUserUseCase;
    private final UserDtoMapper userDtoMapper;
    private final LoginUserUseCase loginUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final ConfirmAccountUseCase confirmAccountUseCase;
    private final GetUsuarioByEmailUseCase getUsuarioByEmailUseCase;


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
            log.error("Erro ao criar usuário: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginUser(@Valid @RequestBody LoginRequestDTO request) {
        try {
            var command = userDtoMapper.toLoginCommand(request);
            LoginUserUseCase.LoginResult result = loginUserUseCase.loginUser(command);
            UserResponseDTO userInfo = userDtoMapper.toResponse(result.usuario());
            LoginResponseDTO response = new LoginResponseDTO(result.token(), userInfo);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Erro durante o login: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new LoginResponseDTO("Credenciais inválidas", null));
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable String id) {
        try {
            Usuario usuarioEncontrado = getUserUseCase.getUserById(id);
            UserResponseDTO response = userDtoMapper.toResponse(usuarioEncontrado);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Usuário não encontrado com id {}: {}", id, e.getMessage());
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

    @PostMapping("/confirm-account")
    public ResponseEntity<String> confirmAccount(@RequestBody ConfirmAccountRequestDTO request) {
        var command = new ConfirmAccountUseCase.ConfirmAccountCommand(request.email(), request.codigo());
        confirmAccountUseCase.confirmAccount(command);
        return ResponseEntity.ok("Conta ativada com sucesso!");

    }

    @GetMapping("/by-email")
    public ResponseEntity<UserResponseDTO> getUsuarioByEmail(@RequestParam String email) {
        try {
            // Delega a chamada para o Use Case e mapeia o resultado
            Usuario usuario = getUsuarioByEmailUseCase.getUsuarioByEmail(email);
            UserResponseDTO response = userDtoMapper.toResponse(usuario);

            return ResponseEntity.ok(response);
        } catch (NoSuchElementException e) {
            // Se o usuário não for encontrado, retorna 404
            return ResponseEntity.notFound().build();
        }
    }
}


