package com.personalproject.GestionIncidencias.repository;

import com.personalproject.GestionIncidencias.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
    boolean existsByDni(String dni);
    boolean existsByRuc(String ruc);
}
