package com.personalproject.GestionIncidencias.exception;

/**
 * La petición es válida pero choca con el estado actual de los datos
 * (por ejemplo, borrar un cliente que tiene solicitudes). Se responde con 409.
 */
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
