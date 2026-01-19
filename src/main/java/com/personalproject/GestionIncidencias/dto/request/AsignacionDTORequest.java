package com.personalproject.GestionIncidencias.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AsignacionDTORequest {
    private Long collaboratorId;
    private Long solicitudId;
}
