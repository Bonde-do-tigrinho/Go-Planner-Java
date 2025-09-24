package com.go.go_planner.infrastructure.config.adapter.in.web.mapper;

import com.go.go_planner.application.port.in.LoginUserUseCase;
import com.go.go_planner.domain.model.Usuario;
import com.go.go_planner.infrastructure.config.adapter.in.web.dto.CreateUserRequestDTO;
import com.go.go_planner.infrastructure.config.adapter.in.web.dto.LoginRequestDTO;
import com.go.go_planner.infrastructure.config.adapter.in.web.dto.UserResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "foto", ignore = true)
    @Mapping(target = "amigos", ignore = true)
    @Mapping(target = "notificacoes", ignore = true)

    Usuario toDomain(CreateUserRequestDTO request);
    LoginUserUseCase.LoginUserCommand toLoginCommand(LoginRequestDTO request);
    UserResponseDTO toResponse(Usuario usuario);
}
