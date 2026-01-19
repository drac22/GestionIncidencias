package com.personalproject.GestionIncidencias.dto.request;


import com.personalproject.GestionIncidencias.enums.TypeSolicitud;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SolicitudDTORequest {
    @NotBlank
    private String motivo;

    @NotNull
    private TypeSolicitud typeSolicitud;

    @NotNull
    private Long client;
}
