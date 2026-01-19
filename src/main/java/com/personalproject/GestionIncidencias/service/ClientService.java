package com.personalproject.GestionIncidencias.service;

import com.personalproject.GestionIncidencias.dto.request.ClientDTORequest;
import com.personalproject.GestionIncidencias.dto.request.ClientRegistrationDTORequest;
import com.personalproject.GestionIncidencias.dto.response.ClientDTOResponse;
import com.personalproject.GestionIncidencias.model.Client;

import java.util.List;

public interface ClientService {
    List<ClientDTOResponse> getListClient();
    ClientDTOResponse getClientById(Long id);
    ClientDTOResponse createClient(ClientRegistrationDTORequest request);
    void deleteClient(Long id);
    Client getEntityById(Long id);
}
