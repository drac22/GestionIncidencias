package com.personalproject.GestionIncidencias.service;

import com.personalproject.GestionIncidencias.dto.request.SoftwareDTORequest;
import com.personalproject.GestionIncidencias.dto.response.SoftwareDTOResponse;

import java.util.List;

public interface SoftwareService {
    List<SoftwareDTOResponse> getSoftwares();
    SoftwareDTOResponse getSoftwareById(Long id);
    SoftwareDTOResponse createSoftware(SoftwareDTORequest soft);
    void deleteSoftware(Long id);
    SoftwareDTOResponse updateSoftwareById(Long id, SoftwareDTORequest request);
}
