package com.personalproject.GestionIncidencias.controller;

import com.personalproject.GestionIncidencias.dto.request.CollaboratorRegistrationDTORequest;
import com.personalproject.GestionIncidencias.dto.response.CollaboratorDTOResponse;
import com.personalproject.GestionIncidencias.service.CollaboratorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collaborator")
@RequiredArgsConstructor
public class CollaboratorController {

    private final CollaboratorService collaboratorService;

    @GetMapping
    public ResponseEntity<List<CollaboratorDTOResponse>> getListCollaborator(){
        return ResponseEntity.ok(collaboratorService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CollaboratorDTOResponse> getCollaboratorById(@PathVariable Long id) {
        return ResponseEntity.ok(collaboratorService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CollaboratorDTOResponse> createCollaborator(@RequestBody @Valid CollaboratorRegistrationDTORequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(collaboratorService.createCollaborator(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCollaboratorById(@PathVariable Long id){
        collaboratorService.deleteCollaborator(id);
        return ResponseEntity.noContent().build();
    }
}
