package com.personalproject.GestionIncidencias.service;

import com.personalproject.GestionIncidencias.dto.request.SolicitudDTORequest;
import com.personalproject.GestionIncidencias.dto.response.SolicitudDTOResponse;
import com.personalproject.GestionIncidencias.enums.StateSolicitud;
import com.personalproject.GestionIncidencias.exception.ResourceNotFoundException;
import com.personalproject.GestionIncidencias.mapper.SolicitudMapper;
import com.personalproject.GestionIncidencias.model.Solicitud;
import com.personalproject.GestionIncidencias.model.User;
import com.personalproject.GestionIncidencias.repository.SolicitudRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SolicitudServiceImpl implements SolicitudService{

    private final SolicitudRepository solicitudRepository;
    private final ClientService clientService;
    private final SolicitudMapper solicitudMapper;

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudDTOResponse> findAllSolicitud() {
        return solicitudRepository.findAll()
                .stream()
                .map(solicitudMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SolicitudDTOResponse findById(Long id) {
        return solicitudMapper.toDTO(getEntityById(id));
    }

    @Transactional
    @Override
    public SolicitudDTOResponse createSolicitud(SolicitudDTORequest request, User user) {
        Solicitud solicitud = solicitudMapper.toEntity(request);
        solicitud.setCreatedAt(LocalDateTime.now());
        solicitud.setStateSolicitud(StateSolicitud.EN_ESPERA);
        // El cliente es siempre el usuario autenticado: nadie puede crear solicitudes a nombre de otro
        solicitud.setClient(clientService.getEntityByUserId(user.getId()));
        solicitudRepository.save(solicitud);
        return solicitudMapper.toDTO(solicitud);
    }

    @Override
    public Solicitud getEntityById(Long id) {
        return solicitudRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la Solicitud con ID: " + id));
    }
}
