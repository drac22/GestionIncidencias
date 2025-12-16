package com.personalproject.GestionIncidencias.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SoftwareDTOResponse {
    private Long id;
    private String name;
}
