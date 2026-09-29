package com.personalproject.GestionIncidencias.service;

import com.personalproject.GestionIncidencias.dto.request.AsignacionDTORequest;
import com.personalproject.GestionIncidencias.dto.response.AsigancionDTOResponse;
import com.personalproject.GestionIncidencias.enums.StateSolicitud;
import com.personalproject.GestionIncidencias.exception.ConflictException;
import com.personalproject.GestionIncidencias.mapper.AsignacionMapper;
import com.personalproject.GestionIncidencias.model.Asignacion;
import com.personalproject.GestionIncidencias.model.Collaborator;
import com.personalproject.GestionIncidencias.model.Solicitud;
import com.personalproject.GestionIncidencias.repository.AsignacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AsignacionServiceImpl implements AsignacionService{

    private final AsignacionRepository asignacionRepository;
    private final CollaboratorService collaboratorService;
    private final SolicitudService solicitudService;
    private final AsignacionMapper asignacionMapper;

    @Override
    @Transactional
    public AsigancionDTOResponse asignacionSolicitud(AsignacionDTORequest request) {
        Collaborator collaborator = collaboratorService.getEntityById(request.getCollaboratorId());
        Solicitud solicitud = solicitudService.getEntityById(request.getSolicitudId());

        if (solicitud.getStateSolicitud() == StateSolicitud.CONCLUIDO) {
            throw new ConflictException("No se puede asignar una solicitud que ya está concluida");
        }
        if (asignacionRepository.existsBySolicitudIdAndCollaboratorId(solicitud.getId(), collaborator.getId())) {
            throw new ConflictException("El colaborador ya está asignado a esta solicitud");
        }

        Asignacion asignacionCreate = new Asignacion();
        asignacionCreate.setAssignedAt(LocalDateTime.now());
        asignacionCreate.setCollaborator(collaborator);
        asignacionCreate.setSolicitud(solicitud);

        // Con el primer colaborador asignado la solicitud empieza a atenderse.
        // No hace falta save(): la entidad está dentro de la transacción y Hibernate guarda el cambio
        if (solicitud.getStateSolicitud() == StateSolicitud.EN_ESPERA) {
            solicitud.setStateSolicitud(StateSolicitud.EN_PROCESO);
        }

        Asignacion asignacionSaved = asignacionRepository.save(asignacionCreate);

        return asignacionMapper.toDto(asignacionSaved);
    }
}
