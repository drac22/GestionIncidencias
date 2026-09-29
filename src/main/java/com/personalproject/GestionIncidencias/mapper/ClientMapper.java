package com.personalproject.GestionIncidencias.mapper;

import com.personalproject.GestionIncidencias.dto.request.ClientDTORequest;
import com.personalproject.GestionIncidencias.dto.response.ClientDTOResponse;
import com.personalproject.GestionIncidencias.dto.response.SoftwareDTOResponse;
import com.personalproject.GestionIncidencias.model.Client;
import com.personalproject.GestionIncidencias.model.ClientSoftware;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    ClientDTOResponse toResponse(Client client);

    // ClientSoftware es la tabla intermedia: en la respuesta se muestra el software, no la relación
    @Mapping(target = "id", source = "software.id")
    @Mapping(target = "name", source = "software.name")
    SoftwareDTOResponse toSoftwareResponse(ClientSoftware clientSoftware);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "softwares", ignore = true)
    Client toEntity(ClientDTORequest request);
}
