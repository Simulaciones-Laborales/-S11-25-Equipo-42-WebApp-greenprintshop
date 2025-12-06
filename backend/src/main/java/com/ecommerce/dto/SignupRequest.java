package com.ecommerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para manejar la solicitud de registro de un nuevo usuario.
 */
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    // Coherente con LoginRequest, usamos 'nombre' como identificador.
    private String nombre; 

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
    
    // Nota: Por simplicidad, no incluimos 'rol' en el DTO de registro. 
    // Asumiremos que los roles son asignados a nivel de servicio.

    // --- Getters y Setters (Necesarios para que Jackson los serialice) ---
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
