package com.go.go_planner.infrastructure.config.adapter.in.web.mapper;

import com.go.go_planner.application.port.in.CreateAtividadeUseCase;
import com.go.go_planner.application.port.in.CreateViagemUseCase.CreateViagemCommand;
import com.go.go_planner.application.port.in.UpdateViagemUseCase;
import com.go.go_planner.infrastructure.config.adapter.in.web.dto.CreateAtividadeRequestDTO;
import com.go.go_planner.infrastructure.config.adapter.in.web.dto.CreateViagemRequestDTO;
import com.go.go_planner.infrastructure.config.adapter.in.web.dto.UpdateViagemRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ViagemDtoMapper {

    @Mapping(target = "criadorId", source = "criadorId")
    CreateViagemCommand toCommand(CreateViagemRequestDTO requestDTO, String criadorId);

    @Mapping(target = "viagemId", source = "viagemId")
    @Mapping(target = "criadorId", source = "criadorId")
    UpdateViagemUseCase.UpdateViagemCommand toUpdateCommand(UpdateViagemRequestDTO dto, String viagemId, String criadorId);


    @Mapping(target = "viagemId", source = "viagemId")
    CreateAtividadeUseCase.CreateAtividadeCommand toAtividadeCommand(String viagemId, CreateAtividadeRequestDTO requestDTO);

}