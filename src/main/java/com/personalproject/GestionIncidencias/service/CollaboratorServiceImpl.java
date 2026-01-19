package com.personalproject.GestionIncidencias.service;

import com.personalproject.GestionIncidencias.dto.request.CollaboratorRegistrationDTORequest;
import com.personalproject.GestionIncidencias.dto.response.CollaboratorDTOResponse;
import com.personalproject.GestionIncidencias.enums.Role;
import com.personalproject.GestionIncidencias.exception.ResourceNotFoundException;
import com.personalproject.GestionIncidencias.mapper.CollaboratorMapper;
import com.personalproject.GestionIncidencias.mapper.UserMapper;
import com.personalproject.GestionIncidencias.model.Collaborator;
import com.personalproject.GestionIncidencias.model.User;
import com.personalproject.GestionIncidencias.repository.CollaboratorRepository;
import com.personalproject.GestionIncidencias.repository.UserRepository;
import com.personalproject.GestionIncidencias.validate.UserValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CollaboratorServiceImpl implements CollaboratorService{

    private final CollaboratorRepository collaboratorRespository;
    private final CollaboratorMapper collaboratorMapper;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserValidator userValidator;

    @Override
    public List<CollaboratorDTOResponse> findAll(){
        return collaboratorRespository.findAll()
                .stream()
                .map(collaboratorMapper::toDto)
                .toList();
    }

    @Override
    public CollaboratorDTOResponse findById(Long id){
        return collaboratorMapper.toDto(getEntityById(id));
    }

    @Transactional
    @Override
    public CollaboratorDTOResponse createCollaborator(CollaboratorRegistrationDTORequest request) {
        userValidator.validateNotExistEmail(request.getUser().getEmail());

        User user = userMapper.toEntity(request.getUser());
        user.setRoles(Set.of(Role.ROLE_COLLABORATOR));
        user.setEnabled(true);
        userRepository.save(user);

        Collaborator collabo = collaboratorMapper.toEntity(request.getCollaborator());
        collabo.setUser(user);
        collaboratorRespository.save(collabo);
        return collaboratorMapper.toDto(collabo);
    }

    @Transactional
    @Override
    public void deleteCollaborator(Long id){
        collaboratorRespository.delete(getEntityById(id));
    }

    @Override
    public Collaborator getEntityById(Long id){
        return collaboratorRespository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el colaborador con ID: " + id));
    }
}