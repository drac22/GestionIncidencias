package com.personalproject.GestionIncidencias.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@JsonPropertyOrder(
        {
                "id",
                "assignedAt",
                "collaboratorId",
                "solicitudId"
        }
)
@Getter
@Setter
public class AsigancionDTOResponse {
    private Long id;
    private Long collaboratorId;
    private Long solicitudId;
    private LocalDateTime assignedAt;
}
