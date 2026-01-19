package com.personalproject.GestionIncidencias.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.personalproject.GestionIncidencias.enums.Occupation;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonPropertyOrder(
        {
                "id",
                "name",
                "phone",
                "occupation",
                "asignaciones"
        }
)
@Getter
@Setter
public class CollaboratorDTOResponse {
    private Long id;
    private String name;
    private String phone;
    private Occupation occupation;
    private List<AsigancionDTOResponse> asignaciones;
}
