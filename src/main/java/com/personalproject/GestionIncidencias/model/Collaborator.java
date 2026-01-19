package com.personalproject.GestionIncidencias.model;

import com.personalproject.GestionIncidencias.enums.Occupation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "colaborador")
public class Collaborator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String phone;

    @Enumerated(EnumType.STRING)
    private Occupation occupation;

    @OneToOne
    @JoinColumn(name = "id_user")
    private User user;

    @OneToMany(mappedBy = "collaborator", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Asignacion> asignaciones;
}
