package com.personalproject.GestionIncidencias.service;

import com.personalproject.GestionIncidencias.dto.request.CollaboratorRegistrationDTORequest;
import com.personalproject.GestionIncidencias.dto.response.CollaboratorDTOResponse;
import com.personalproject.GestionIncidencias.model.Collaborator;

import java.util.List;

public interface CollaboratorService {
    List<CollaboratorDTOResponse> findAll();
    CollaboratorDTOResponse findById(Long id);
    CollaboratorDTOResponse createCollaborator(CollaboratorRegistrationDTORequest request);
    void deleteCollaborator(Long id);
    Collaborator getEntityById(Long id);
}
