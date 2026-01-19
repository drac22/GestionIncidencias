package com.personalproject.GestionIncidencias.mapper;

import com.personalproject.GestionIncidencias.dto.response.AsigancionDTOResponse;
import com.personalproject.GestionIncidencias.model.Asignacion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AsignacionMapper {
    @Mapping(source = "collaborator.id", target = "collaboratorId")
    @Mapping(source = "solicitud.id", target = "solicitudId")
    AsigancionDTOResponse toDto(Asignacion asignacion);
}
