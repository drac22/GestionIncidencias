package com.personalproject.GestionIncidencias.auth;

import com.personalproject.GestionIncidencias.auth.dto.AuthResponse;
import com.personalproject.GestionIncidencias.auth.dto.LoginRequest;
import com.personalproject.GestionIncidencias.exception.InvalidTokenException;
import com.personalproject.GestionIncidencias.model.RefreshToken;
import com.personalproject.GestionIncidencias.model.User;
import com.personalproject.GestionIncidencias.repository.RefreshTokenRepository;
import com.personalproject.GestionIncidencias.security.JwtProperties;
import com.personalproject.GestionIncidencias.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public AuthResponse login(LoginRequest request) {
        // Lanza BadCredentialsException / DisabledException si falla (se responden como 401)
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        return issueTokens((User) auth.getPrincipal());
    }

    /**
     * Rotación de refresh tokens: cada refresh token se usa una sola vez.
     * Si llega uno ya revocado, alguien lo robó y lo está reutilizando:
     * se revocan todas las sesiones del usuario.
     * noRollbackFor evita que el revocado se deshaga al lanzar la excepción.
     */
    @Transactional(noRollbackFor = InvalidTokenException.class)
    public AuthResponse refresh(String rawRefreshToken) {
        RefreshToken stored = refreshTokenRepository.findByTokenHash(hash(rawRefreshToken))
                .orElseThrow(() -> new InvalidTokenException("Refresh token inválido"));

        if (stored.isRevoked()) {
            refreshTokenRepository.revokeAllByUser(stored.getUser());
            throw new InvalidTokenException("Refresh token reutilizado. Se cerraron todas las sesiones por seguridad");
        }
        if (stored.isExpired()) {
            throw new InvalidTokenException("Refresh token expirado, inicia sesión nuevamente");
        }
        if (!stored.getUser().isEnabled()) {
            throw new InvalidTokenException("La cuenta está deshabilitada");
        }

        stored.setRevoked(true);
        return issueTokens(stored.getUser());
    }

    @Transactional
    public void logout(String rawRefreshToken) {
        // Idempotente: si el token no existe o ya estaba revocado no se informa nada
        refreshTokenRepository.findByTokenHash(hash(rawRefreshToken))
                .ifPresent(token -> token.setRevoked(true));
    }

    private AuthResponse issueTokens(User user) {
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = createRefreshToken(user);
        return new AuthResponse(accessToken, refreshToken, jwtService.getAccessTokenExpirationSeconds());
    }

    private String createRefreshToken(User user) {
        byte[] bytes = new byte[32];
        SECURE_RANDOM.nextBytes(bytes);
        String rawToken = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setTokenHash(hash(rawToken));
        refreshToken.setUser(user);
        refreshToken.setCreatedAt(Instant.now());
        refreshToken.setExpiresAt(Instant.now().plus(jwtProperties.refreshTokenExpiration()));
        refreshToken.setRevoked(false);
        refreshTokenRepository.save(refreshToken);

        return rawToken;
    }

    private static String hash(String value) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 no disponible", e);
        }
    }
}
