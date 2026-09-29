package com.personalproject.GestionIncidencias.repository;

import com.personalproject.GestionIncidencias.model.Solicitud;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {
    boolean existsByClientId(Long clientId);

    @Override
    @EntityGraph(attributePaths = {"client", "client.softwares", "client.softwares.software"})
    List<Solicitud> findAll();
}
