package com.personalproject.GestionIncidencias.service;

import com.personalproject.GestionIncidencias.dto.request.SoftwareDTORequest;
import com.personalproject.GestionIncidencias.exception.BadRequestException;
import com.personalproject.GestionIncidencias.exception.ConflictException;
import com.personalproject.GestionIncidencias.mapper.SoftMapper;
import com.personalproject.GestionIncidencias.model.Software;
import com.personalproject.GestionIncidencias.repository.SoftwareRepository;
import com.personalproject.GestionIncidencias.validate.SoftwareValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SoftwareServiceImplTest {

    @Mock
    private SoftwareRepository softwareRepository;

    @Mock
    private SoftMapper softMapper;

    private SoftwareServiceImpl softwareService;
    private Software software;

    @BeforeEach
    void setUp() {
        // El validador real, con el repositorio simulado: así se prueba también su lógica
        softwareService = new SoftwareServiceImpl(new SoftwareValidator(softwareRepository), softwareRepository, softMapper);
        software = new Software(1L, "ERP");
        when(softwareRepository.findById(1L)).thenReturn(Optional.of(software));
    }

    @Test
    void eliminar_softwareAsignadoAClientes_lanzaConflicto() {
        when(softwareRepository.isAssignedToAnyClient(1L)).thenReturn(true);

        assertThatThrownBy(() -> softwareService.deleteSoftware(1L))
                .isInstanceOf(ConflictException.class);
        verify(softwareRepository, never()).delete(any());
    }

    @Test
    void actualizar_conNombreDeOtroSoftware_lanzaError() {
        when(softwareRepository.existsByNameAndIdNot("CRM", 1L)).thenReturn(true);

        assertThatThrownBy(() -> softwareService.updateSoftwareById(1L, SoftwareDTORequest.builder().name("CRM").build()))
                .isInstanceOf(BadRequestException.class);
        assertThat(software.getName()).isEqualTo("ERP");
    }

    @Test
    void actualizar_conNombreLibre_cambiaElNombre() {
        when(softwareRepository.existsByNameAndIdNot("ERP Cloud", 1L)).thenReturn(false);

        softwareService.updateSoftwareById(1L, SoftwareDTORequest.builder().name("ERP Cloud").build());

        assertThat(software.getName()).isEqualTo("ERP Cloud");
    }
}
