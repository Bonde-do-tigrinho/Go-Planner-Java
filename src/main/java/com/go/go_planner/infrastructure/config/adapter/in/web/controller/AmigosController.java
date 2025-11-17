package com.go.go_planner.infrastructure.config.adapter.in.web.controller;

import com.go.go_planner.application.port.in.GetAmigosUseCase;
import com.go.go_planner.domain.model.Usuario;
import com.go.go_planner.infrastructure.config.adapter.in.web.dto.UserResponseDTO;
import com.go.go_planner.infrastructure.config.adapter.in.web.mapper.UserDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class AmigosController {

    private final GetAmigosUseCase getAmigosUseCase;
    private final UserDtoMapper userDtoMapper;

    @GetMapping("/{userId}")
    public ResponseEntity<List<UserResponseDTO>> getAmigos(@PathVariable String userId) {
        try {
            List<Usuario> amigos = getAmigosUseCase.getAmigos(userId);
            List<UserResponseDTO> response = amigos.stream()
                    .map(userDtoMapper::toResponse)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(response);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}