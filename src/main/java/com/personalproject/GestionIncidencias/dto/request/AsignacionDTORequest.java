package com.personalproject.GestionIncidencias.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AsignacionDTORequest {
    @NotNull
    private Long collaboratorId;

    @NotNull
    private Long solicitudId;
}
