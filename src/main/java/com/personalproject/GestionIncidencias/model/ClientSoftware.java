package com.personalproject.GestionIncidencias.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "client_software")
@Getter
@Setter
public class ClientSoftware {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "software_id", nullable = false)
    private Software software;

    @Column(nullable = false)
    private LocalDateTime timestamp;
}
