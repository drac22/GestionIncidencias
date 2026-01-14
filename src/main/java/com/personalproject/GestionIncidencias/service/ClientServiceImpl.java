package com.personalproject.GestionIncidencias.service;

import com.personalproject.GestionIncidencias.dto.request.ClientDTORequest;
import com.personalproject.GestionIncidencias.dto.request.ClientRegistrationDTORequest;
import com.personalproject.GestionIncidencias.dto.request.ClientSoftwareDTORequest;
import com.personalproject.GestionIncidencias.dto.request.SoftwareDTORequest;
import com.personalproject.GestionIncidencias.dto.response.ClientDTOResponse;
import com.personalproject.GestionIncidencias.dto.response.SoftwareDTOResponse;
import com.personalproject.GestionIncidencias.enums.ClientType;
import com.personalproject.GestionIncidencias.enums.Role;
import com.personalproject.GestionIncidencias.exception.BadRequestException;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService{

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final ClientMapper clientMapper;
    private final UserMapper userMapper;
    private final SoftMapper softMapper;
    private final SoftwareRepository softwareRepository;

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

        validate(request);

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

    private void validate(ClientRegistrationDTORequest request) {
        Map<String,String> errors = new HashMap<>();

        ClientDTORequest client = request.getClient();

        if (client.getTypeClient() == ClientType.EMPRESA) {

            if (client.getRuc() == null || client.getRuc().isBlank()) {
                errors.put("ruc", "El RUC es obligatorio para clientes empresa");
            }
        }

        if (client.getTypeClient() == ClientType.PERSONA_NATURAL) {

            if (client.getDni() == null || client.getDni().isBlank()) {
                errors.put("dni", "El DNI es obligatorio para clientes persona");
            }
        }

        if(userRepository.existsByEmail(request.getUser().getEmail()))
            errors.put("email", "El email ya está registrado");

        if(request.getClient().getDni() != null &&
                clientRepository.existsByDni(request.getClient().getDni()))
            errors.put("dni", "El DNI ya está registrado");

        if(request.getClient().getRuc() != null &&
                clientRepository.existsByRuc(request.getClient().getRuc()))
            errors.put("ruc", "El RUC ya está registrado");

        if (request.getClient().getSoftwares() != null && !request.getClient().getSoftwares().isEmpty()) {

            Set<Long> softwareIds = new HashSet<>(request.getClient().getSoftwares());

            if (softwareIds.size() != request.getClient().getSoftwares().size()) {
                errors.put("softwares", "Los softwares se repiten");
            }
        }

        if(!errors.isEmpty())
            throw new BadRequestException("Errores de lógica", errors);
    }

    private void validateSoftwaresIdDuplicate(ClientRegistrationDTORequest request, Map<String,String> errors){
        List<Long> softwareIds = request.getClient().getSoftwares();
        if (softwareIds == null || softwareIds.isEmpty()) return;
        Set<Long> unico = new HashSet<>(softwareIds);
        if (softwareIds.size() != unico.size()){
            errors.put("List<Softwares>", "No se permiten softwares duplicados");
        }
    }

    @Override
    public void deleteClient(Long id) {
        Client clientExist = clientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cliente con ID: " + id + " no encontrado"));
        clientRepository.delete(clientExist);
    }
}
