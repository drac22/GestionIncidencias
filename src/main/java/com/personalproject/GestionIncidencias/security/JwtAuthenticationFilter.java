package com.personalproject.GestionIncidencias.security;

import com.personalproject.GestionIncidencias.exception.InvalidTokenException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

/**
 * Lee el header "Authorization: Bearer <token>" y, si el token es válido,
 * autentica al usuario para esta petición.
 * No es un @Component a propósito: si lo fuera, Spring Boot lo registraría
 * también como filtro normal y se ejecutaría dos veces. Se crea en SecurityConfig.
 */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final HandlerExceptionResolver exceptionResolver;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // En /auth/** el cliente puede enviar un access token vencido junto al refresh token
        return request.getRequestURI().startsWith(request.getContextPath() + "/auth/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header == null || !header.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String email = jwtService.extractUsername(header.substring(BEARER_PREFIX.length()));
            UserDetails user = userDetailsService.loadUserByUsername(email);

            // Si la cuenta está deshabilitada no se autentica y las rutas protegidas responden 401
            if (user.isEnabled()) {
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (JwtException | UsernameNotFoundException ex) {
            // Delegamos al GlobalExceptionHandler para responder 401 con el formato ApiError
            SecurityContextHolder.clearContext();
            exceptionResolver.resolveException(request, response, null,
                    new InvalidTokenException("Token inválido o expirado"));
            return;
        }

        filterChain.doFilter(request, response);
    }
}
