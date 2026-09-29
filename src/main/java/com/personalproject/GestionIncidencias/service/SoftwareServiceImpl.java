package com.personalproject.GestionIncidencias.service;

import com.personalproject.GestionIncidencias.dto.request.SoftwareDTORequest;
import com.personalproject.GestionIncidencias.dto.response.SoftwareDTOResponse;
import com.personalproject.GestionIncidencias.exception.ConflictException;
import com.personalproject.GestionIncidencias.exception.ResourceNotFoundException;
import com.personalproject.GestionIncidencias.mapper.SoftMapper;
import com.personalproject.GestionIncidencias.model.Software;
import com.personalproject.GestionIncidencias.repository.SoftwareRepository;
import com.personalproject.GestionIncidencias.validate.SoftwareValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SoftwareServiceImpl implements SoftwareService {

    private final SoftwareValidator softwareValidator;
    private final SoftwareRepository softwareRepository;
    private final SoftMapper softMapper;

    @Override
    @Transactional(readOnly = true)
    public List<SoftwareDTOResponse> getSoftwares() {
        return softwareRepository.findAll().stream().map(softMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SoftwareDTOResponse getSoftwareById(Long id) {
        return softMapper.toResponse(getEntityById(id));
    }

    @Override
    @Transactional
    public SoftwareDTOResponse createSoftware(SoftwareDTORequest soft) {
        softwareValidator.validateNotExists(soft.getName());
        Software software = softMapper.toEntity(soft);
        return softMapper.toResponse(softwareRepository.save(software));
    }

    @Override
    @Transactional
    public void deleteSoftware(Long id) {
        Software software = getEntityById(id);
        if (softwareRepository.isAssignedToAnyClient(id)) {
            throw new ConflictException("No se puede eliminar el software porque está asignado a uno o más clientes");
        }
        softwareRepository.delete(software);
    }

    @Override
    @Transactional
    public SoftwareDTOResponse updateSoftwareById(Long id, SoftwareDTORequest request) {
        Software software = getEntityById(id);
        softwareValidator.validateNameAvailable(request.getName(), id);
        software.setName(request.getName());
        return softMapper.toResponse(software);
    }

    private Software getEntityById(Long id) {
        return softwareRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Software con ID: " + id + " no encontrado"));
    }
}
