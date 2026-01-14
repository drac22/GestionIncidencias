package com.personalproject.GestionIncidencias.validate;

import com.personalproject.GestionIncidencias.exception.BadRequestException;
import com.personalproject.GestionIncidencias.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;

    public void validateNotExistEmail(String email) {
        if (userRepository.existsByEmail(email)){
            throw new BadRequestException("El email ya existe");
    }
}
}
