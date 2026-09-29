package com.personalproject.GestionIncidencias.service;

import com.personalproject.GestionIncidencias.dto.request.AsignacionDTORequest;
import com.personalproject.GestionIncidencias.enums.StateSolicitud;
import com.personalproject.GestionIncidencias.exception.ConflictException;
import com.personalproject.GestionIncidencias.mapper.AsignacionMapper;
import com.personalproject.GestionIncidencias.model.Asignacion;
import com.personalproject.GestionIncidencias.model.Collaborator;
import com.personalproject.GestionIncidencias.model.Solicitud;
import com.personalproject.GestionIncidencias.repository.AsignacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AsignacionServiceImplTest {

    @Mock
    private AsignacionRepository asignacionRepository;

    @Mock
    private CollaboratorService collaboratorService;

    @Mock
    private SolicitudService solicitudService;

    @Mock
    private AsignacionMapper asignacionMapper;

    @InjectMocks
    private AsignacionServiceImpl asignacionService;

    private Collaborator collaborator;
    private Solicitud solicitud;
    private AsignacionDTORequest request;

    @BeforeEach
    void setUp() {
        collaborator = new Collaborator();
        collaborator.setId(1L);

        solicitud = new Solicitud();
        solicitud.setId(2L);

        request = new AsignacionDTORequest();
        request.setCollaboratorId(1L);
        request.setSolicitudId(2L);

        when(collaboratorService.getEntityById(1L)).thenReturn(collaborator);
        when(solicitudService.getEntityById(2L)).thenReturn(solicitud);
    }

    @Test
    void asignar_solicitudEnEspera_pasaAEnProceso() {
        solicitud.setStateSolicitud(StateSolicitud.EN_ESPERA);
        when(asignacionRepository.save(any(Asignacion.class))).thenAnswer(inv -> inv.getArgument(0));

        asignacionService.asignacionSolicitud(request);

        assertThat(solicitud.getStateSolicitud()).isEqualTo(StateSolicitud.EN_PROCESO);
        verify(asignacionRepository).save(any(Asignacion.class));
    }

    @Test
    void asignar_solicitudConcluida_lanzaConflicto() {
        solicitud.setStateSolicitud(StateSolicitud.CONCLUIDO);

        assertThatThrownBy(() -> asignacionService.asignacionSolicitud(request))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("concluida");
        verify(asignacionRepository, never()).save(any());
    }

    @Test
    void asignar_colaboradorYaAsignado_lanzaConflicto() {
        solicitud.setStateSolicitud(StateSolicitud.EN_PROCESO);
        when(asignacionRepository.existsBySolicitudIdAndCollaboratorId(2L, 1L)).thenReturn(true);

        assertThatThrownBy(() -> asignacionService.asignacionSolicitud(request))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("ya está asignado");
        verify(asignacionRepository, never()).save(any());
    }
}
