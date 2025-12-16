package com.personalproject.GestionIncidencias.mapper;

import com.personalproject.GestionIncidencias.dto.request.SoftwareDTORequest;
import com.personalproject.GestionIncidencias.dto.response.SoftwareDTOResponse;
import com.personalproject.GestionIncidencias.model.Software;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SoftMapper {
    SoftwareDTOResponse toResponse(Software software);
    Software toEntity(SoftwareDTORequest request);
}
