package com.go.go_planner.infrastructure.config.adapter.in.web.mapper;

import com.go.go_planner.domain.model.Usuario;
import com.go.go_planner.infrastructure.config.adapter.in.web.dto.CreateUserRequestDTO;
import com.google.firebase.remoteconfig.internal.TemplateResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring") // <-- ADD THI
public interface UserDtoMapper {

    // Mapeia o DTO de requisição para o modelo de domínio
    // Como os nomes dos campos (name, email, password) são iguais, o MapStruct faz tudo sozinho.
    // O @Mapping aqui é apenas para ignorar campos que não existem no DTO.
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cpf", ignore = true)
    @Mapping(target = "foto", ignore = true)
    @Mapping(target = "amigos", ignore = true)
    @Mapping(target = "notificacoes", ignore = true)
    Usuario toDomain(CreateUserRequestDTO request);

    // Mapeia o modelo de domínio para o DTO de resposta
    // Como os nomes (id, nome, email) são iguais em Usuario e UserResponse,
    // o MapStruct também faz a conversão automaticamente.
    // Note que os campos "senha", "cpf", etc., são ignorados por padrão porque
    // não existem no UserResponse, o que é ótimo para a segurança.
    TemplateResponse.UserResponse toResponse(Usuario usuario);
}
