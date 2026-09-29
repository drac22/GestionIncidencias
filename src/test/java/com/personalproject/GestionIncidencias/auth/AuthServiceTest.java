package com.personalproject.GestionIncidencias.auth;

import com.personalproject.GestionIncidencias.auth.dto.AuthResponse;
import com.personalproject.GestionIncidencias.enums.Role;
import com.personalproject.GestionIncidencias.exception.InvalidTokenException;
import com.personalproject.GestionIncidencias.model.RefreshToken;
import com.personalproject.GestionIncidencias.model.User;
import com.personalproject.GestionIncidencias.repository.RefreshTokenRepository;
import com.personalproject.GestionIncidencias.security.JwtProperties;
import com.personalproject.GestionIncidencias.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    private AuthService authService;
    private User user;

    @BeforeEach
    void setUp() {
        JwtProperties properties = new JwtProperties("no-se-usa", Duration.ofMinutes(15), Duration.ofDays(7));
        authService = new AuthService(authenticationManager, jwtService, properties, refreshTokenRepository);

        user = new User();
        user.setEmail("client@test.com");
        user.setRoles(Set.of(Role.ROLE_CLIENT));
        user.setEnabled(true);
    }

    @Test
    void refresh_valido_revocaElAnteriorYEmiteTokensNuevos() {
        RefreshToken stored = storedToken(false, Instant.now().plus(Duration.ofDays(1)));
        when(refreshTokenRepository.findByTokenHash(anyString())).thenReturn(Optional.of(stored));
        when(jwtService.generateAccessToken(user)).thenReturn("nuevo-access");

        AuthResponse response = authService.refresh("refresh-original");

        assertThat(stored.isRevoked()).isTrue();
        assertThat(response.accessToken()).isEqualTo("nuevo-access");
        assertThat(response.refreshToken()).isNotBlank().isNotEqualTo("refresh-original");
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    void refresh_reutilizado_revocaTodasLasSesiones() {
        RefreshToken stored = storedToken(true, Instant.now().plus(Duration.ofDays(1)));
        when(refreshTokenRepository.findByTokenHash(anyString())).thenReturn(Optional.of(stored));

        assertThatThrownBy(() -> authService.refresh("token-robado"))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessageContaining("reutilizado");
        verify(refreshTokenRepository).revokeAllByUser(user);
        verify(refreshTokenRepository, never()).save(any());
    }

    @Test
    void refresh_expirado_lanzaExcepcion() {
        RefreshToken stored = storedToken(false, Instant.now().minus(Duration.ofMinutes(1)));
        when(refreshTokenRepository.findByTokenHash(anyString())).thenReturn(Optional.of(stored));

        assertThatThrownBy(() -> authService.refresh("token-viejo"))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessageContaining("expirado");
        verify(refreshTokenRepository, never()).save(any());
    }

    @Test
    void refresh_inexistente_lanzaExcepcion() {
        when(refreshTokenRepository.findByTokenHash(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.refresh("no-existe"))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    void logout_revocaElToken() {
        RefreshToken stored = storedToken(false, Instant.now().plus(Duration.ofDays(1)));
        when(refreshTokenRepository.findByTokenHash(anyString())).thenReturn(Optional.of(stored));

        authService.logout("refresh");

        assertThat(stored.isRevoked()).isTrue();
    }

    private RefreshToken storedToken(boolean revoked, Instant expiresAt) {
        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setRevoked(revoked);
        token.setExpiresAt(expiresAt);
        token.setCreatedAt(Instant.now());
        return token;
    }
}
