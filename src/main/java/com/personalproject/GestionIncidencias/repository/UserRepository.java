package com.personalproject.GestionIncidencias.repository;

import com.personalproject.GestionIncidencias.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
