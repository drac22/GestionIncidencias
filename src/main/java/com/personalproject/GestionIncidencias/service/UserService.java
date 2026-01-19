package com.personalproject.GestionIncidencias.service;

import com.personalproject.GestionIncidencias.dto.response.UserDTOResponse;

import java.util.List;

public interface UserService{
    UserDTOResponse findById(Long id);
    List<UserDTOResponse> findAllUser();
    boolean existsByEmail(String email);
}