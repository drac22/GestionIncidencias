package com.personalproject.GestionIncidencias.mapper;

import com.personalproject.GestionIncidencias.dto.request.CollaboratorDTORequest;
import com.personalproject.GestionIncidencias.dto.response.CollaboratorDTOResponse;
import com.personalproject.GestionIncidencias.model.Collaborator;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = AsignacionMapper.class)
public interface CollaboratorMapper {
    CollaboratorDTOResponse toDto (Collaborator collaborator);
    Collaborator toEntity(CollaboratorDTORequest collaboratorDTORequest);
}
