package com.personalproject.GestionIncidencias.security;

import com.personalproject.GestionIncidencias.enums.Role;
import com.personalproject.GestionIncidencias.model.User;
import com.personalproject.GestionIncidencias.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Crea el primer usuario ADMIN al arrancar. Sin él nadie podría usar
 * los endpoints de administración (por ejemplo, crear colaboradores).
 * Solo actúa si se definen ADMIN_EMAIL y ADMIN_PASSWORD y el email no existe.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AdminUserInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email:}")
    private String adminEmail;

    @Value("${app.admin.password:}")
    private String adminPassword;

    @Override
    public void run(ApplicationArguments args) {
        if (adminEmail.isBlank() || adminPassword.isBlank()) {
            log.info("ADMIN_EMAIL/ADMIN_PASSWORD no definidos: no se crea el usuario administrador");
            return;
        }
        if (userRepository.existsByEmail(adminEmail)) {
            return;
        }

        User admin = new User();
        admin.setEmail(adminEmail);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setRoles(Set.of(Role.ROLE_ADMIN));
        admin.setEnabled(true);
        userRepository.save(admin);
        log.info("Usuario administrador creado: {}", adminEmail);
    }
}
