package com.personalproject.GestionIncidencias.service;

import com.personalproject.GestionIncidencias.dto.request.SoftwareDTORequest;
import com.personalproject.GestionIncidencias.dto.response.SoftwareDTOResponse;
import com.personalproject.GestionIncidencias.exception.ResourceNotFoundException;
import com.personalproject.GestionIncidencias.mapper.SoftMapper;
import lombok.RequiredArgsConstructor;
import com.personalproject.GestionIncidencias.model.Software;
import org.springframework.stereotype.Service;
import com.personalproject.GestionIncidencias.repository.SoftwareRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SoftwareServiceImpl implements SoftwareService {

    private final SoftwareRepository softwareRepository;
    private final SoftMapper softMapper;

    @Override
    public List<SoftwareDTOResponse> getSoftwares() {
        return softwareRepository.findAll().stream().map(softMapper::toResponse).toList();
    }

    @Override
    public SoftwareDTOResponse getSoftwareById(Long id) {
        Software software = softwareRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Software con ID: " + id + " no encontrado"));
        return softMapper.toResponse(software);
    }

    @Override
    public SoftwareDTOResponse createSoftware(SoftwareDTORequest soft) {
        Software software = softMapper.toEntity(soft);
        return softMapper.toResponse(softwareRepository.save(software));
    }

    @Override
    public void deleteSoftware(Long id) {
        Software existSoftware = softwareRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Software no encontrado"));
        softwareRepository.delete(existSoftware);
    }

    @Override
    public SoftwareDTOResponse updateSoftwareById(Long id, SoftwareDTORequest request) {
        Software software = softwareRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Software no encontrado"));
        software.setName(request.getName());
        softwareRepository.save(software);
        return softMapper.toResponse(software);
    }
}
