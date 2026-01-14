package com.personalproject.GestionIncidencias.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.personalproject.GestionIncidencias.enums.ClientType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientDTOResponse {
    private Long id;
    private String name;
    private String phone;
    private ClientType typeClient;
    private String dni;
    private String ruc;
    private List<SoftwareDTOResponse> softwares;
}
