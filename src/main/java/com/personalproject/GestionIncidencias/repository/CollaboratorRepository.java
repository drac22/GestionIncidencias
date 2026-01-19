package com.personalproject.GestionIncidencias.repository;

import com.personalproject.GestionIncidencias.model.Collaborator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollaboratorRepository extends JpaRepository<Collaborator, Long> {

}
