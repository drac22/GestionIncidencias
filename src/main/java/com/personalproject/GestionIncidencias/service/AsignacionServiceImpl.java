package com.personalproject.GestionIncidencias.service;

import com.personalproject.GestionIncidencias.dto.request.AsignacionDTORequest;
import com.personalproject.GestionIncidencias.dto.response.AsigancionDTOResponse;
import com.personalproject.GestionIncidencias.mapper.AsignacionMapper;
import com.personalproject.GestionIncidencias.model.Asignacion;
import com.personalproject.GestionIncidencias.model.Collaborator;
import com.personalproject.GestionIncidencias.model.Solicitud;
import com.personalproject.GestionIncidencias.repository.AsignacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AsignacionServiceImpl implements AsignacionService{

    private final AsignacionRepository asignacionRepository;
    private final CollaboratorService collaboratorService;
    private final SolicitudService solicitudService;
    private final AsignacionMapper asignacionMapper;

    @Override
    public AsigancionDTOResponse asignacionSolicitud(AsignacionDTORequest request) {
        Collaborator collaborator = collaboratorService.getEntityById(request.getCollaboratorId());
        Solicitud solicitud = solicitudService.getEntityById(request.getSolicitudId());

        Asignacion asignacionCreate = new Asignacion();
        asignacionCreate.setAssignedAt(LocalDateTime.now());
        asignacionCreate.setCollaborator(collaborator);
        asignacionCreate.setSolicitud(solicitud);

        Asignacion asignacionSaved = asignacionRepository.save(asignacionCreate);

        return asignacionMapper.toDto(asignacionSaved);
    }
}
