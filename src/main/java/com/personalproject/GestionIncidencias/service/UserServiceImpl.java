package com.personalproject.GestionIncidencias.service;

import com.personalproject.GestionIncidencias.dto.response.UserDTOResponse;
import com.personalproject.GestionIncidencias.exception.ResourceNotFoundException;
import com.personalproject.GestionIncidencias.mapper.UserMapper;
import com.personalproject.GestionIncidencias.model.User;
import com.personalproject.GestionIncidencias.repository.RefreshTokenRepository;
import com.personalproject.GestionIncidencias.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public List<UserDTOResponse> findAllUser() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTOResponse findById(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No se encontro el User con ID: " + id));
        return userMapper.toResponse(user);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public void deleteUser(User user) {
        // Primero los refresh tokens: tienen una clave foránea hacia el usuario
        refreshTokenRepository.deleteAllByUser(user);
        userRepository.delete(user);
    }
}
