package com.go.go_planner.infrastructure.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalException {

    // Este método será chamado automaticamente sempre que um 'IllegalArgumentException' for lançado
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException e) {
        // Retorna um JSON estruturado e um status 400
        return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    // Este método será chamado automaticamente sempre que um 'IllegalStateException' for lançado
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalStateException(IllegalStateException e) {
        // Retorna um JSON e um status 409 Conflict, que é mais apropriado para "estado inválido"
        return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.CONFLICT);
    }
}
