package com.personalproject.GestionIncidencias.dto.request;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ClientRegistrationDTORequest {
    @Valid
    private UserDTORequest user;

    @Valid
    private ClientDTORequest client;
}
