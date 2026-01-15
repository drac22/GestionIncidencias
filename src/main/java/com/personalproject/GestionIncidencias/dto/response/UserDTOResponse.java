package com.personalproject.GestionIncidencias.dto.response;

import com.personalproject.GestionIncidencias.enums.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserDTOResponse {
    private String email;
    private Set<Role> roles;
    private boolean enabled;
}
