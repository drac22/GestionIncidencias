package com.personalproject.GestionIncidencias.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SoftwareDTORequest {
    @NotBlank
    private String name;
}
