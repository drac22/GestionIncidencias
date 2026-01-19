package com.personalproject.GestionIncidencias.service;

import com.personalproject.GestionIncidencias.dto.request.AsignacionDTORequest;
import com.personalproject.GestionIncidencias.dto.response.AsigancionDTOResponse;

public interface AsignacionService {
    AsigancionDTOResponse asignacionSolicitud(AsignacionDTORequest request);
}
