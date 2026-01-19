package com.personalproject.GestionIncidencias.controller;

import com.personalproject.GestionIncidencias.dto.request.SolicitudDTORequest;
import com.personalproject.GestionIncidencias.dto.response.SolicitudDTOResponse;
import com.personalproject.GestionIncidencias.service.SolicitudService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
@RequiredArgsConstructor
public class SolicitudController {

    private final SolicitudService solicitudService;

    @GetMapping
    public ResponseEntity<List<SolicitudDTOResponse>> getListSolicitudes(){
        return ResponseEntity.ok(solicitudService.findAllSolicitud());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudDTOResponse> getSolicitudById(@PathVariable Long id){
        return ResponseEntity.ok(solicitudService.findById(id));
    }

    @PostMapping
    public ResponseEntity<SolicitudDTOResponse> createSolicitud(@RequestBody @Valid SolicitudDTORequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(solicitudService.createSolicitud(request));
    }
}
