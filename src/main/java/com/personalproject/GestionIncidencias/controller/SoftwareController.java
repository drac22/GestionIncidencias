package com.personalproject.GestionIncidencias.controller;

import com.personalproject.GestionIncidencias.dto.request.SoftwareDTORequest;
import com.personalproject.GestionIncidencias.dto.response.SoftwareDTOResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.personalproject.GestionIncidencias.service.SoftwareService;

import java.util.List;

@RestController
@RequestMapping("/api/software")
@RequiredArgsConstructor
public class SoftwareController {

    private final SoftwareService softwareService;

    @GetMapping
    public ResponseEntity<List<SoftwareDTOResponse>> getAllSoftwares(){
        List<SoftwareDTOResponse> softwares = softwareService.getSoftwares();
        return ResponseEntity.ok(softwares);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SoftwareDTOResponse> getSoftwareById(@PathVariable Long id){
        SoftwareDTOResponse software = softwareService.getSoftwareById(id);
        return ResponseEntity.ok(software);
    }

    @PostMapping
    public ResponseEntity<SoftwareDTOResponse> createSoftware(@RequestBody @Valid SoftwareDTORequest request){
        SoftwareDTOResponse softwareCreated = softwareService.createSoftware(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(softwareCreated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSoftwareById(@PathVariable Long id){
        softwareService.deleteSoftware(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<SoftwareDTOResponse> updateSoftware(@PathVariable Long id, @RequestBody @Valid SoftwareDTORequest request){
        SoftwareDTOResponse softwareUpdate = softwareService.updateSoftwareById(id, request);
        return ResponseEntity.ok(softwareUpdate);
    }
}
