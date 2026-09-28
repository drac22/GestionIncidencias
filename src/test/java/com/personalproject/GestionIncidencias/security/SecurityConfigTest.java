package com.personalproject.GestionIncidencias.security;

import com.personalproject.GestionIncidencias.auth.AuthController;
import com.personalproject.GestionIncidencias.auth.AuthService;
import com.personalproject.GestionIncidencias.auth.dto.AuthResponse;
import com.personalproject.GestionIncidencias.controller.SoftwareController;
import com.personalproject.GestionIncidencias.enums.Role;
import com.personalproject.GestionIncidencias.model.User;
import com.personalproject.GestionIncidencias.service.SoftwareService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {SoftwareController.class, AuthController.class})
@Import({SecurityConfig.class, JwtService.class})
@EnableConfigurationProperties(JwtProperties.class)
@TestPropertySource(properties = {
        "jwt.secret=" + SecurityConfigTest.SECRET,
        "jwt.access-token-expiration=15m",
        "jwt.refresh-token-expiration=7d"
})
class SecurityConfigTest {

    static final String SECRET = "clave-de-test-con-mas-de-32-caracteres-0123456789";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private SoftwareService softwareService;

    @MockitoBean
    private AuthService authService;

    private User admin;
    private User client;

    @BeforeEach
    void setUp() {
        admin = user("admin@test.com", Role.ROLE_ADMIN);
        client = user("client@test.com", Role.ROLE_CLIENT);
        when(userDetailsService.loadUserByUsername(admin.getEmail())).thenReturn(admin);
        when(userDetailsService.loadUserByUsername(client.getEmail())).thenReturn(client);
        when(softwareService.getSoftwares()).thenReturn(List.of());
    }

    @Test
    void sinToken_rutaProtegida_responde401() throws Exception {
        mockMvc.perform(get("/api/software"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Se requiere autenticación"));
    }

    @Test
    void tokenInvalido_responde401() throws Exception {
        mockMvc.perform(get("/api/software").header(HttpHeaders.AUTHORIZATION, "Bearer token-falso"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Token inválido o expirado"));
    }

    @Test
    void tokenExpirado_responde401() throws Exception {
        JwtService expiredTokens = new JwtService(new JwtProperties(SECRET, Duration.ofMinutes(-1), Duration.ofDays(7)));
        String expired = expiredTokens.generateAccessToken(admin);

        mockMvc.perform(get("/api/software").header(HttpHeaders.AUTHORIZATION, bearer(expired)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Token inválido o expirado"));
    }

    @Test
    void cliente_puedeConsultarSoftware() throws Exception {
        mockMvc.perform(get("/api/software").header(HttpHeaders.AUTHORIZATION, bearer(jwtService.generateAccessToken(client))))
                .andExpect(status().isOk());
    }

    @Test
    void cliente_noPuedeBorrarSoftware_responde403() throws Exception {
        mockMvc.perform(delete("/api/software/1").header(HttpHeaders.AUTHORIZATION, bearer(jwtService.generateAccessToken(client))))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("No tienes permisos para acceder a este recurso"));
    }

    @Test
    void admin_puedeBorrarSoftware() throws Exception {
        mockMvc.perform(delete("/api/software/1").header(HttpHeaders.AUTHORIZATION, bearer(jwtService.generateAccessToken(admin))))
                .andExpect(status().isNoContent());
    }

    @Test
    void login_esPublico_aunqueLlegueUnTokenVencido() throws Exception {
        when(authService.login(any())).thenReturn(new AuthResponse("access", "refresh", 900));

        mockMvc.perform(post("/auth/login")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token-vencido")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"admin@test.com\",\"password\":\"secreta123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access"))
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }

    @Test
    void login_credencialesIncorrectas_responde401() throws Exception {
        when(authService.login(any())).thenThrow(new BadCredentialsException("bad"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"admin@test.com\",\"password\":\"incorrecta\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Email o contraseña incorrectos"));
    }

    private static User user(String email, Role role) {
        User user = new User();
        user.setEmail(email);
        user.setPassword("no-importa");
        user.setRoles(Set.of(role));
        user.setEnabled(true);
        return user;
    }

    private static String bearer(String token) {
        return "Bearer " + token;
    }
}
