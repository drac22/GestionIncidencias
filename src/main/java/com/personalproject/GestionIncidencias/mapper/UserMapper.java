package com.personalproject.GestionIncidencias.mapper;

import com.personalproject.GestionIncidencias.dto.request.UserDTORequest;
import com.personalproject.GestionIncidencias.dto.response.UserDTOResponse;
import com.personalproject.GestionIncidencias.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserDTORequest request);
    UserDTOResponse toResponse(User user);
}
