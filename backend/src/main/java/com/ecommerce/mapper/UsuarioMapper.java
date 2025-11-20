package com.ecommerce.mapper;

import com.ecommerce.dto.usuario.UsuarioRequest;
import com.ecommerce.dto.usuario.UsuarioResponse;
import com.ecommerce.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    Usuario toEntity(UsuarioRequest request);

    UsuarioResponse toResponse(Usuario usuario);

    void updateEntity(@MappingTarget Usuario usuario, UsuarioRequest request);
}

