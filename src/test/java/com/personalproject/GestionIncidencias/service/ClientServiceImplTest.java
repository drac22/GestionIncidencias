package com.personalproject.GestionIncidencias.service;

import com.personalproject.GestionIncidencias.exception.ConflictException;
import com.personalproject.GestionIncidencias.model.Client;
import com.personalproject.GestionIncidencias.model.User;
import com.personalproject.GestionIncidencias.repository.ClientRepository;
import com.personalproject.GestionIncidencias.repository.SolicitudRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ClientServiceImpl clientService;

    @Test
    void eliminar_clienteConSolicitudes_lanzaConflictoYNoBorraNada() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client(1L)));
        when(solicitudRepository.existsByClientId(1L)).thenReturn(true);

        assertThatThrownBy(() -> clientService.deleteClient(1L))
                .isInstanceOf(ConflictException.class);
        verify(clientRepository, never()).delete(any());
        verify(userService, never()).deleteUser(any());
    }

    @Test
    void eliminar_clienteSinSolicitudes_borraClienteYSuUsuario() {
        Client client = client(1L);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(solicitudRepository.existsByClientId(1L)).thenReturn(false);

        clientService.deleteClient(1L);

        verify(clientRepository).delete(client);
        verify(userService).deleteUser(client.getUser());
    }

    private static Client client(Long id) {
        Client client = new Client();
        client.setId(id);
        client.setUser(new User());
        return client;
    }
}
