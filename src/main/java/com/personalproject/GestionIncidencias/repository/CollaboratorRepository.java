package com.personalproject.GestionIncidencias.repository;

import com.personalproject.GestionIncidencias.model.Collaborator;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollaboratorRepository extends JpaRepository<Collaborator, Long> {

    @Override
    @EntityGraph(attributePaths = {"asignaciones"})
    List<Collaborator> findAll();
}
