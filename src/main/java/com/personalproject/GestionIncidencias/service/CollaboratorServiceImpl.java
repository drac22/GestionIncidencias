package com.personalproject.GestionIncidencias.service;

import com.personalproject.GestionIncidencias.dto.request.CollaboratorRegistrationDTORequest;
import com.personalproject.GestionIncidencias.dto.response.CollaboratorDTOResponse;
import com.personalproject.GestionIncidencias.enums.Role;
import com.personalproject.GestionIncidencias.exception.ConflictException;
import com.personalproject.GestionIncidencias.exception.ResourceNotFoundException;
import com.personalproject.GestionIncidencias.mapper.CollaboratorMapper;
import com.personalproject.GestionIncidencias.mapper.UserMapper;
import com.personalproject.GestionIncidencias.model.Collaborator;
import com.personalproject.GestionIncidencias.model.User;
import com.personalproject.GestionIncidencias.repository.AsignacionRepository;
import com.personalproject.GestionIncidencias.repository.CollaboratorRepository;
import com.personalproject.GestionIncidencias.repository.UserRepository;
import com.personalproject.GestionIncidencias.validate.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CollaboratorServiceImpl implements CollaboratorService{

    private final CollaboratorRepository collaboratorRepository;
    private final AsignacionRepository asignacionRepository;
    private final CollaboratorMapper collaboratorMapper;
    private final UserRepository userRepository;
    private final UserService userService;
    private final UserMapper userMapper;
    private final UserValidator userValidator;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<CollaboratorDTOResponse> findAll(){
        return collaboratorRepository.findAll()
                .stream()
                .map(collaboratorMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CollaboratorDTOResponse findById(Long id){
        return collaboratorMapper.toDto(getEntityById(id));
    }

    @Transactional
    @Override
    public CollaboratorDTOResponse createCollaborator(CollaboratorRegistrationDTORequest request) {
        userValidator.validateNotExistEmail(request.getUser().getEmail());

        User user = userMapper.toEntity(request.getUser());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(Role.ROLE_COLLABORATOR));
        user.setEnabled(true);
        userRepository.save(user);

        Collaborator collabo = collaboratorMapper.toEntity(request.getCollaborator());
        collabo.setUser(user);
        collaboratorRepository.save(collabo);
        return collaboratorMapper.toDto(collabo);
    }

    @Transactional
    @Override
    public void deleteCollaborator(Long id){
        Collaborator collaborator = getEntityById(id);
        // Las asignaciones son el historial de las solicitudes: no se pierden al borrar
        if (asignacionRepository.existsByCollaboratorId(id)) {
            throw new ConflictException("No se puede eliminar el colaborador porque tiene asignaciones registradas");
        }
        User user = collaborator.getUser();
        collaboratorRepository.delete(collaborator);
        if (user != null) {
            userService.deleteUser(user);
        }
    }

    @Override
    public Collaborator getEntityById(Long id){
        return collaboratorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el colaborador con ID: " + id));
    }
}
