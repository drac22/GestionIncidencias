package com.personalproject.GestionIncidencias.model;

import com.personalproject.GestionIncidencias.enums.ClientType;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
@Table(name = "cliente")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    @Column(unique = true)
    private String dni;

    @Column(unique = true)
    private String ruc;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClientType typeClient;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClientSoftware> softwares = new ArrayList<>();
}
