# Gestión de Incidencias – API REST

API RESTful desarrollada con Spring Boot para la gestión de incidencias, usuarios, clientes, colaboradores y software.
Incluye operaciones CRUD completas, validaciones, persistencia en base de datos, seguridad con JWT y documentación interactiva.

## Tecnologías utilizadas

- **Java 21**
- **Spring Boot 3**
- **Spring Web**
- **Spring Data JPA**
- **Spring Validation**
- **Spring Security + JWT (autenticación y autorización)**
- **MapStruct (mapeo de DTOs)**
- **MySQL**
- **Docker & Docker Compose**
- **Swagger**
- **Lombok**
- **JUnit + Mockito**

## Seguridad (JWT)

La API implementa un flujo de autenticación basado en **JSON Web Tokens (JWT)**:

1. El usuario inicia sesión enviando sus credenciales.
2. El servidor genera un **token JWT** que contiene el rol del usuario.
3. Todas las rutas protegidas requieren enviar el token en el header.
4. Roles soportados:
- **ADMIN**
- **CLIENTE**
- **COLABORADOR**
- **USUARIO**

## Arquitectura del proyecto

El proyecto utiliza **arquitectura por capas**, ideal para aplicaciones medianas.  
La separación es clara y mantiene el código limpio:

- **Controller** → expone los endpoints REST.
- **Service** → contiene la lógica de negocio.
- **Repository** → maneja el acceso a datos (JPA).
- **DTOs** → define modelos para intercambio de datos.
- **Mapper (MapStruct)** → transforma entidades ↔ DTOs.
- **Security** → configuración JWT y autorización por roles.
- **Exception** → manejo global de errores (ControllerAdvice).

## Documentación

Swagger está disponible una vez levantado el proyecto:

http://localhost:8080/swagger-ui/index.html

## Docker

El proyecto cuenta con:
- Dockerfile → para contenerizar la API
- docker-compose.yml → para levantar API + MySQL
