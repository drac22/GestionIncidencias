package com.personalproject.GestionIncidencias.model;

import com.personalproject.GestionIncidencias.enums.Occupation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    private User user;

    // Sin cascade: borrar un colaborador no debe borrar el historial de asignaciones
    @OneToMany(mappedBy = "collaborator")
    private List<Asignacion> asignaciones = new ArrayList<>();
}
