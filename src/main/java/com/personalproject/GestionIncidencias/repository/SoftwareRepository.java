package com.personalproject.GestionIncidencias.repository;

import com.personalproject.GestionIncidencias.model.Software;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SoftwareRepository extends JpaRepository<Software, Long> {
}
