package com.ecommerce.dto.usuario;

import lombok.Data;

@Data
public class UsuarioRequest {
    private String nombre;
    private String apellido;
    private String email;
    private Boolean activo;
}

