package com.personalproject.GestionIncidencias.dto.request;

import com.personalproject.GestionIncidencias.enums.ClientType;
import com.personalproject.GestionIncidencias.model.ClientSoftware;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ClientDTORequest {
    @NotBlank
    private String name;

    @NotBlank
    private String phone;

    @Size(min = 8, max = 8)
    private String dni;
    @Size(min = 11, max = 11)
    private String ruc;

    @NotNull
    private ClientType typeClient;

    private List<Long> softwares;
}
