package com.go.go_planner.infrastructure.config.adapter.in.web.mapper;

import com.go.go_planner.domain.model.Usuario;
import com.go.go_planner.infrastructure.config.adapter.in.web.dto.CreateUserRequestDTO;
import com.go.go_planner.infrastructure.config.adapter.in.web.dto.UserResponseDTO;
import com.google.firebase.remoteconfig.internal.TemplateResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {

    // Mapeia o DTO de requisição para o modelo de domínio
    // Como os nomes dos campos (nome, email, password) são iguais, o MapStruct faz tudo sozinho.
    // O @Mapping aqui é apenas para ignorar campos que não existem no DTO.
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "nome", source = "nome")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "senha", source = "senha")
    @Mapping(target = "cpf", ignore = true)
    @Mapping(target = "foto", ignore = true)
    @Mapping(target = "amigos", ignore = true)
    @Mapping(target = "notificacoes", ignore = true)
    Usuario toDomain(CreateUserRequestDTO request);

    UserResponseDTO toResponse(Usuario usuario);
}
