package com.go.go_planner.infrastructure.config.adapter.in.web.controller;

import com.go.go_planner.application.port.in.CreateUserUseCase;
import com.go.go_planner.application.port.in.GetUserUseCase;
import com.go.go_planner.application.port.in.LoginUserUseCase;
import com.go.go_planner.domain.model.Usuario;
import com.go.go_planner.infrastructure.config.adapter.in.web.dto.*;
import com.go.go_planner.infrastructure.config.adapter.in.web.mapper.UserDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.go.go_planner.application.port.in.GetUsuarioByEmailUseCase;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.NoSuchElementException;

import java.net.URI;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final GetUserUseCase getUserUseCase; // Adicione o novo UseCase
    private final UserDtoMapper userDtoMapper;
    private final LoginUserUseCase loginUserUseCase;
    private final GetUsuarioByEmailUseCase getUsuarioByEmailUseCase;



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

    @PatchMapping("update-profile")
    public ResponseEntity updateProfile(String id){
        return ResponseEntity.ok().body("Perfil atualizado com sucesso!");
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


