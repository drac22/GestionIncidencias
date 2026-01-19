package com.personalproject.GestionIncidencias.service;

import com.personalproject.GestionIncidencias.dto.request.ClientRegistrationDTORequest;
import com.personalproject.GestionIncidencias.dto.response.ClientDTOResponse;
import com.personalproject.GestionIncidencias.dto.response.SoftwareDTOResponse;
import com.personalproject.GestionIncidencias.enums.Role;
import com.personalproject.GestionIncidencias.exception.ResourceNotFoundException;
import com.personalproject.GestionIncidencias.mapper.ClientMapper;
import com.personalproject.GestionIncidencias.mapper.SoftMapper;
import com.personalproject.GestionIncidencias.mapper.UserMapper;
import com.personalproject.GestionIncidencias.model.Client;
import com.personalproject.GestionIncidencias.model.ClientSoftware;
import com.personalproject.GestionIncidencias.model.Software;
import com.personalproject.GestionIncidencias.model.User;
import com.personalproject.GestionIncidencias.repository.ClientRepository;
import com.personalproject.GestionIncidencias.repository.SoftwareRepository;
import com.personalproject.GestionIncidencias.repository.UserRepository;
import com.personalproject.GestionIncidencias.validate.ClientValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService{

    private final SoftwareRepository softwareRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;

    private final ClientValidator clientValidator;
    private final ClientMapper clientMapper;
    private final UserMapper userMapper;
    private final SoftMapper softMapper;

    @Override
    public List<ClientDTOResponse> getListClient() {
        return clientRepository.findAll()
                .stream()
                .map(client -> {
                    ClientDTOResponse response = clientMapper.toResponse(client);
                    List<SoftwareDTOResponse> softwares = client.getSoftwares()
                            .stream()
                            .map(cs -> softMapper.toResponse(cs.getSoftware()))
                            .toList();
                    response.setSoftwares(softwares);
                    return response;
                })
                .toList();
    }

    @Override
    public ClientDTOResponse getClientById(Long id) {
        Client client = clientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cliente con ID: " + id + " no encontrado"));
        ClientDTOResponse response = clientMapper.toResponse(client);
        List<SoftwareDTOResponse> softwares = client.getSoftwares().stream().map(cs -> softMapper.toResponse(cs.getSoftware())).toList();
        response.setSoftwares(softwares);
        return response;
    }

    @Override
    @Transactional
    public ClientDTOResponse createClient(ClientRegistrationDTORequest request) {
        clientValidator.validateForCreation(request);
        User user = userMapper.toEntity(request.getUser());
        user.setRoles(Set.of(Role.ROLE_CLIENT));
        user.setEnabled(true);
        userRepository.save(user);
        Client client = clientMapper.toEntity(request.getClient());
        if (request.getClient().getSoftwares() != null) {
            for (Long dto : request.getClient().getSoftwares()) {
                Software software = softwareRepository.findById(dto)
                        .orElseThrow(() -> new ResourceNotFoundException("Software con ID: " + dto + " no encontrado"));
                ClientSoftware cs = new ClientSoftware();
                cs.setClient(client);
                cs.setSoftware(software);
                cs.setTimestamp(LocalDateTime.now());
                client.getSoftwares().add(cs);
            }
        }
        client.setUser(user);
        clientRepository.save(client);
        List<SoftwareDTOResponse> softwareResponses =
                client.getSoftwares().stream()
                        .map(cs -> SoftwareDTOResponse.builder()
                                .id(cs.getSoftware().getId())
                                .name(cs.getSoftware().getName())
                                .build())
                        .toList();
        ClientDTOResponse clientDTOResponse = clientMapper.toResponse(client);
        clientDTOResponse.setSoftwares(softwareResponses);
        return clientDTOResponse;
    }

    @Override
    public void deleteClient(Long id) {
        Client clientExist = clientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cliente con ID: " + id + " no encontrado"));
        clientRepository.delete(clientExist);
    }

    @Override
    public Client getEntityById(Long id) {
        return clientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cliente con ID: " + id + " no encontrado"));
    }
}
