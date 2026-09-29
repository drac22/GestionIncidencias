package com.personalproject.GestionIncidencias.repository;

import com.personalproject.GestionIncidencias.model.Software;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SoftwareRepository extends JpaRepository<Software, Long> {
    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    @Query("SELECT COUNT(cs) > 0 FROM ClientSoftware cs WHERE cs.software.id = :softwareId")
    boolean isAssignedToAnyClient(Long softwareId);
}
