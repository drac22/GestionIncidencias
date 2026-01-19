package com.personalproject.GestionIncidencias.service;

import com.personalproject.GestionIncidencias.dto.request.SolicitudDTORequest;
import com.personalproject.GestionIncidencias.dto.response.SolicitudDTOResponse;
import com.personalproject.GestionIncidencias.model.Solicitud;

import java.util.List;

public interface SolicitudService {
    List<SolicitudDTOResponse> findAllSolicitud();
    SolicitudDTOResponse findById(Long id);
    SolicitudDTOResponse createSolicitud(SolicitudDTORequest request);
    Solicitud getEntityById(Long id);
}
