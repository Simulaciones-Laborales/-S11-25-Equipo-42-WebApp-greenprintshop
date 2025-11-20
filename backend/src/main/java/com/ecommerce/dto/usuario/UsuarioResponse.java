package com.ecommerce.dto.usuario;

import lombok.Data;

@Data
public class UsuarioResponse {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private Boolean activo;
}

