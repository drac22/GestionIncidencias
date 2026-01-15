package com.personalproject.GestionIncidencias.service;

import com.personalproject.GestionIncidencias.dto.response.UserDTOResponse;
import com.personalproject.GestionIncidencias.exception.ResourceNotFoundException;
import com.personalproject.GestionIncidencias.mapper.UserMapper;
import com.personalproject.GestionIncidencias.model.User;
import com.personalproject.GestionIncidencias.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDTOResponse findById(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No se encontro el User con ID: " + id));
        return userMapper.toResponse(user);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}