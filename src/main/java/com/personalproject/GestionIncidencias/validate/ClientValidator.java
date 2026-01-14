package com.personalproject.GestionIncidencias.validate;

import com.personalproject.GestionIncidencias.dto.request.ClientDTORequest;
import com.personalproject.GestionIncidencias.dto.request.ClientRegistrationDTORequest;
import com.personalproject.GestionIncidencias.enums.ClientType;
import com.personalproject.GestionIncidencias.exception.BadRequestException;
import com.personalproject.GestionIncidencias.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ClientValidator {

    private final ClientRepository clientRepository;
    private final UserValidator userValidator;

    public void validateForCreation(ClientRegistrationDTORequest request) {
        userValidator.validateNotExistEmail(request.getUser().getEmail());
        validateRequiredDocuments(request.getClient());
        validateNotExists(request.getClient());
        validateSoftwaresDuplicate(request.getClient());
    }

    private void validateRequiredDocuments(ClientDTORequest client) {
        if (client.getTypeClient() == ClientType.EMPRESA &&
                (client.getRuc() == null || client.getRuc().isBlank())) {
            throw new BadRequestException("El RUC es obligatorio para empresas");
        }

        if (client.getTypeClient() == ClientType.PERSONA_NATURAL &&
                (client.getDni() == null || client.getDni().isBlank())) {
            throw new BadRequestException("El DNI es obligatorio para personas naturales");
        }
    }

    private void validateNotExists(ClientDTORequest client) {
        if (client.getDni() != null && clientRepository.existsByDni(client.getDni())) {
            throw new BadRequestException("El DNI ya existe");
        }

        if (client.getRuc() != null && clientRepository.existsByRuc(client.getRuc())) {
            throw new BadRequestException("El RUC ya existe");
        }
    }

    private void validateSoftwaresDuplicate(ClientDTORequest request){
        List<Long> softwareIds = request.getSoftwares();
        if (softwareIds == null || softwareIds.isEmpty()) return;
        Set<Long> unico = new HashSet<>(softwareIds);
        if (softwareIds.size() != unico.size()){
            throw new BadRequestException("Softwares duplicados");
        }
    }
}
