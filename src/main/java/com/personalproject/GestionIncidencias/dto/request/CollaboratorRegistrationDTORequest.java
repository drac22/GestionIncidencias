package com.personalproject.GestionIncidencias.dto.request;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CollaboratorRegistrationDTORequest {
    @Valid
    private CollaboratorDTORequest collaborator;

    @Valid
    private UserDTORequest user;
}
