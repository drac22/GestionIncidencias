package com.personalproject.GestionIncidencias.controller;

import com.personalproject.GestionIncidencias.dto.response.UserDTOResponse;
import com.personalproject.GestionIncidencias.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDTOResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok(userService.findById(id));
    }

}
