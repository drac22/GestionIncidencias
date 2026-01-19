package com.personalproject.GestionIncidencias.dto.request;

import com.personalproject.GestionIncidencias.enums.Occupation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CollaboratorDTORequest {
    @NotBlank
    private String name;

    @NotBlank
    private String phone;

    @NotNull
    private Occupation occupation;
}
