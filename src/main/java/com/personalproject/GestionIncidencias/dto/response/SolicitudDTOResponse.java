package com.personalproject.GestionIncidencias.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.personalproject.GestionIncidencias.enums.StateSolicitud;
import com.personalproject.GestionIncidencias.enums.TypeSolicitud;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@JsonPropertyOrder(
        {
                "id",
                "motivo",
                "createdAt",
                "deleteAt",
                "stateSolicitud",
                "typeSolicitud",
                "client"
        }
)
@Getter
@Setter
public class SolicitudDTOResponse {
    private Long id;
    private String motivo;
    private LocalDateTime createdAt;
    private LocalDateTime deleteAt;
    private StateSolicitud stateSolicitud;
    private TypeSolicitud typeSolicitud;
    private ClientDTOResponse client;
}
