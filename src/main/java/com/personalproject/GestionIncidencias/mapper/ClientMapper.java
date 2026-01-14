package com.personalproject.GestionIncidencias.mapper;

import com.personalproject.GestionIncidencias.dto.request.ClientDTORequest;
import com.personalproject.GestionIncidencias.dto.request.ClientRegistrationDTORequest;
import com.personalproject.GestionIncidencias.dto.response.ClientDTOResponse;
import com.personalproject.GestionIncidencias.model.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    @Mapping(target = "softwares", ignore = true)
    ClientDTOResponse toResponse(Client client);

    @Mapping(target = "softwares", ignore = true)
    Client toEntity(ClientDTORequest request);
}
