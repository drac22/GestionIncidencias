package com.personalproject.GestionIncidencias.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "software")
public class Software {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_software")
    private Long id;

    @Column(name = "name_software", nullable = false, length = 100)
    private String name;
}
