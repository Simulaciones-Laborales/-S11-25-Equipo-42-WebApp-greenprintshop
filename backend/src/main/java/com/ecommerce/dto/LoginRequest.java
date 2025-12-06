package com.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para manejar la solicitud de login del usuario.
 */
public class LoginRequest {
    @NotBlank
    // Usamos 'nombre' ya que es el campo de identificacion en nuestra entidad Usuario.
    private String nombre; 

    @NotBlank
    private String password;

    // --- Getters y Setters (Necesarios para que Jackson los serialice) ---
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
