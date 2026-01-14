package com.personalproject.GestionIncidencias.validate;

import com.personalproject.GestionIncidencias.exception.BadRequestException;
import com.personalproject.GestionIncidencias.repository.SoftwareRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SoftwareValidator {

    private final SoftwareRepository softwareRepository;

    public void validateNotExists(String name){
        if (softwareRepository.existsByName(name)){
            throw new BadRequestException("El nombre del software ya existe");
        }
    }
}
