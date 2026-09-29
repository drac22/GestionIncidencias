package com.personalproject.GestionIncidencias.service;

import com.personalproject.GestionIncidencias.dto.request.SolicitudDTORequest;
import com.personalproject.GestionIncidencias.enums.StateSolicitud;
import com.personalproject.GestionIncidencias.enums.TypeSolicitud;
import com.personalproject.GestionIncidencias.mapper.SolicitudMapper;
import com.personalproject.GestionIncidencias.model.Client;
import com.personalproject.GestionIncidencias.model.Solicitud;
import com.personalproject.GestionIncidencias.model.User;
import com.personalproject.GestionIncidencias.repository.SolicitudRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SolicitudServiceImplTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private ClientService clientService;

    @Mock
    private SolicitudMapper solicitudMapper;

    @InjectMocks
    private SolicitudServiceImpl solicitudService;

    @Test
    void crear_usaElClienteDelUsuarioAutenticado() {
        User user = new User();
        user.setId(7L);
        Client clientDelUsuario = new Client();
        clientDelUsuario.setId(3L);

        SolicitudDTORequest request = new SolicitudDTORequest();
        request.setMotivo("No carga el sistema");
        request.setTypeSolicitud(TypeSolicitud.SERVIDOR_SIN_RESPUESTA);

        when(solicitudMapper.toEntity(request)).thenReturn(new Solicitud());
        when(clientService.getEntityByUserId(7L)).thenReturn(clientDelUsuario);

        solicitudService.createSolicitud(request, user);

        ArgumentCaptor<Solicitud> saved = ArgumentCaptor.forClass(Solicitud.class);
        verify(solicitudRepository).save(saved.capture());
        assertThat(saved.getValue().getClient()).isSameAs(clientDelUsuario);
        assertThat(saved.getValue().getStateSolicitud()).isEqualTo(StateSolicitud.EN_ESPERA);
        assertThat(saved.getValue().getCreatedAt()).isNotNull();
    }
}
