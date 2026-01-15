package com.personalproject.GestionIncidencias.service;

import com.personalproject.GestionIncidencias.dto.request.UserDTORequest;
import com.personalproject.GestionIncidencias.dto.response.UserDTOResponse;

public interface UserService{
    UserDTOResponse findById(Long id);
    boolean existsByEmail(String email);
}