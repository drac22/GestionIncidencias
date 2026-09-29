package com.personalproject.GestionIncidencias.service;

import com.personalproject.GestionIncidencias.dto.request.ClientRegistrationDTORequest;
import com.personalproject.GestionIncidencias.dto.response.ClientDTOResponse;
import com.personalproject.GestionIncidencias.enums.Role;
import com.personalproject.GestionIncidencias.exception.ConflictException;
import com.personalproject.GestionIncidencias.exception.ResourceNotFoundException;
import com.personalproject.GestionIncidencias.mapper.ClientMapper;
import com.personalproject.GestionIncidencias.mapper.UserMapper;
import com.personalproject.GestionIncidencias.model.Client;
import com.personalproject.GestionIncidencias.model.ClientSoftware;
import com.personalproject.GestionIncidencias.model.Software;
import com.personalproject.GestionIncidencias.model.User;
import com.personalproject.GestionIncidencias.repository.ClientRepository;
import com.personalproject.GestionIncidencias.repository.SoftwareRepository;
import com.personalproject.GestionIncidencias.repository.SolicitudRepository;
import com.personalproject.GestionIncidencias.repository.UserRepository;
import com.personalproject.GestionIncidencias.validate.ClientValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService{

    private final SoftwareRepository softwareRepository;
    private final ClientRepository clientRepository;
    private final SolicitudRepository solicitudRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final ClientValidator clientValidator;
    private final ClientMapper clientMapper;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ClientDTOResponse> getListClient() {
        return clientRepository.findAll()
                .stream()
                .map(clientMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ClientDTOResponse getClientById(Long id) {
        return clientMapper.toResponse(getEntityById(id));
    }

    @Override
    @Transactional
    public ClientDTOResponse createClient(ClientRegistrationDTORequest request) {
        clientValidator.validateForCreation(request);
        User user = userMapper.toEntity(request.getUser());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(Role.ROLE_CLIENT));
        user.setEnabled(true);
        userRepository.save(user);
        Client client = clientMapper.toEntity(request.getClient());
        if (request.getClient().getSoftwares() != null) {
            for (Long softwareId : request.getClient().getSoftwares()) {
                Software software = softwareRepository.findById(softwareId)
                        .orElseThrow(() -> new ResourceNotFoundException("Software con ID: " + softwareId + " no encontrado"));
                ClientSoftware cs = new ClientSoftware();
                cs.setClient(client);
                cs.setSoftware(software);
                cs.setTimestamp(LocalDateTime.now());
                client.getSoftwares().add(cs);
            }
        }
        client.setUser(user);
        clientRepository.save(client);
        return clientMapper.toResponse(client);
    }

    @Override
    @Transactional
    public void deleteClient(Long id) {
        Client client = getEntityById(id);
        if (solicitudRepository.existsByClientId(id)) {
            throw new ConflictException("No se puede eliminar el cliente porque tiene solicitudes registradas");
        }
        User user = client.getUser();
        clientRepository.delete(client);
        // Sin su perfil de cliente, la cuenta no debe poder seguir iniciando sesión
        if (user != null) {
            userService.deleteUser(user);
        }
    }

    @Override
    public Client getEntityById(Long id) {
        return clientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cliente con ID: " + id + " no encontrado"));
    }

    @Override
    public Client getEntityByUserId(Long userId) {
        return clientRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("El usuario no tiene un perfil de cliente"));
    }
}
