package com.personalproject.GestionIncidencias.repository;

import com.personalproject.GestionIncidencias.model.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {
}
