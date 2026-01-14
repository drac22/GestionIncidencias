package com.personalproject.GestionIncidencias.controller;

import com.personalproject.GestionIncidencias.dto.request.ClientDTORequest;
import com.personalproject.GestionIncidencias.dto.request.ClientRegistrationDTORequest;
import com.personalproject.GestionIncidencias.dto.response.ClientDTOResponse;
import com.personalproject.GestionIncidencias.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @GetMapping
    public ResponseEntity<List<ClientDTOResponse>> getAllClient(){
        List<ClientDTOResponse> clients = clientService.getListClient();
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDTOResponse> findByIdClient(@PathVariable Long id){
        ClientDTOResponse client = clientService.getClientById(id);
        return ResponseEntity.ok(client);
    }

    @PostMapping
    public ResponseEntity<ClientDTOResponse> createClient(
            @Valid @RequestBody ClientRegistrationDTORequest request) {

        ClientDTOResponse clientCreate = clientService.createClient(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(clientCreate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id){
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

}
