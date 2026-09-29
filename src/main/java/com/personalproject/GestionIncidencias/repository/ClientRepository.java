package com.personalproject.GestionIncidencias.repository;

import com.personalproject.GestionIncidencias.model.Client;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    boolean existsByDni(String dni);
    boolean existsByRuc(String ruc);

    Optional<Client> findByUserId(Long userId);

    // Trae los clientes con sus softwares en una sola consulta (evita el problema N+1)
    @Override
    @EntityGraph(attributePaths = {"softwares", "softwares.software"})
    List<Client> findAll();
}
