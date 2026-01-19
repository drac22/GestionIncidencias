package com.personalproject.GestionIncidencias.controller;

import com.personalproject.GestionIncidencias.dto.request.AsignacionDTORequest;
import com.personalproject.GestionIncidencias.dto.response.AsigancionDTOResponse;
import com.personalproject.GestionIncidencias.service.AsignacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/asignacion")
@RequiredArgsConstructor
public class AsignacionController {

    private final AsignacionService asignacionService;

    @PostMapping
    public ResponseEntity<AsigancionDTOResponse> asignarCollaborator(@RequestBody AsignacionDTORequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(asignacionService.asignacionSolicitud(request));
    }
}
