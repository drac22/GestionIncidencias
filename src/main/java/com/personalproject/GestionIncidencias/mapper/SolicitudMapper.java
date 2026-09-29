package com.personalproject.GestionIncidencias.mapper;

import com.personalproject.GestionIncidencias.dto.request.SolicitudDTORequest;
import com.personalproject.GestionIncidencias.dto.response.SolicitudDTOResponse;
import com.personalproject.GestionIncidencias.model.Solicitud;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

// uses = ClientMapper: reutiliza el mapeo del cliente (incluidos sus softwares) en vez de generar uno propio
@Mapper(componentModel = "spring", uses = ClientMapper.class)
public interface SolicitudMapper {
    SolicitudDTOResponse toDTO(Solicitud solicitud);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "deleteAt", ignore = true)
    @Mapping(target = "stateSolicitud", ignore = true)
    @Mapping(target = "client", ignore = true)
    Solicitud toEntity(SolicitudDTORequest request);
}
