package com.personalproject.GestionIncidencias.model;

import com.personalproject.GestionIncidencias.enums.StateSolicitud;
import com.personalproject.GestionIncidencias.enums.TypeSolicitud;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "solicitud")
@Getter
@Setter
public class Solicitud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String motivo;

    private LocalDateTime createdAt;

    private LocalDateTime deleteAt;

//    @ManyToOne
//    @JoinColumn(name = "collaborator_id")
//    private Collaborator coordinador;

    @Enumerated(EnumType.STRING)
    private StateSolicitud stateSolicitud;

    @Enumerated(EnumType.STRING)
    private TypeSolicitud typeSolicitud;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}
