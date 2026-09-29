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

    // Al editar, el nombre puede repetirse solo si es el del mismo software
    public void validateNameAvailable(String name, Long softwareId){
        if (softwareRepository.existsByNameAndIdNot(name, softwareId)){
            throw new BadRequestException("El nombre del software ya existe");
        }
    }
}
