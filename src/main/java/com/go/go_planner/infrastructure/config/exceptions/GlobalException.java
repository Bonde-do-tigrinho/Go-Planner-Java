package com.go.go_planner.infrastructure.config.exceptions;

import com.go.go_planner.infrastructure.config.adapter.in.web.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalException {

    // 1. Erro 400 - Argumento Inválido (Regra de Negócio)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ErrorResponseDTO err = new ErrorResponseDTO(
                Instant.now(),
                status.value(),
                "Bad Request", // Título do erro
                e.getMessage(), // Mensagem que você passou no 'throw'
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(err);
    }

    // 2. Erro 409 - Estado Inválido (Conflitos)
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalStateException(IllegalStateException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;

        ErrorResponseDTO err = new ErrorResponseDTO(
                Instant.now(),
                status.value(),
                "State Conflict",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(err);
    }

    // 3. Erro 403 - Acesso Negado (Segurança)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDTO> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;

        ErrorResponseDTO err = new ErrorResponseDTO(
                Instant.now(),
                status.value(),
                "Access Denied",
                "Você não tem permissão para realizar esta ação.", // Mensagem mais amigável
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(err);
    }

    // 4. Erro 404 - Não Encontrado (Adicionei este pois você usa muito o .orElseThrow)
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFoundException(NoSuchElementException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        ErrorResponseDTO err = new ErrorResponseDTO(
                Instant.now(),
                status.value(),
                "Resource Not Found",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(err);
    }

    // 5. Erro 400 - Validação de Campos (@Valid / @NotNull)
    // Esse é útil para mostrar qual campo do JSON está errado
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        // Pega todos os erros de campo e junta em uma string só
        String errorMessages = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorResponseDTO err = new ErrorResponseDTO(
                Instant.now(),
                status.value(),
                "Validation Error",
                errorMessages,
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(err);
    }
}
