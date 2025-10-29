// src/main/java/com/go/go_planner/infrastructure/config/adapter/in/web/mapper/ViagemDtoMapper.java

package com.go.go_planner.infrastructure.config.adapter.in.web.mapper;

import com.go.go_planner.domain.model.Viagem;
import com.go.go_planner.infrastructure.config.adapter.in.web.dto.CreateViagemRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;



@Mapper(componentModel = "spring")
public interface ViagemDtoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "favoritada", constant = "false")
    Viagem toDomain(CreateViagemRequestDTO requestDTO);
}