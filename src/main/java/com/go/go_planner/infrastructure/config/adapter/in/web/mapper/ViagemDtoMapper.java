package com.go.go_planner.infrastructure.config.adapter.in.web.mapper;
import com.go.go_planner.application.port.in.CreateViagemUseCase.CreateViagemCommand;
import com.go.go_planner.infrastructure.config.adapter.in.web.dto.CreateViagemRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ViagemDtoMapper {

    // Adicione este novo método.
    // Note que ele recebe o DTO e o ID do criador separadamente.
    @Mapping(target = "criadorId", source = "criadorId") // Mapeia o segundo argumento para o campo criadorId
    CreateViagemCommand toCommand(CreateViagemRequestDTO requestDTO, String criadorId);

}