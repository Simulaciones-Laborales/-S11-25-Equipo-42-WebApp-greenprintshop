package com.ecommerce.dto;

import java.util.List;

/**
 * DTO para manejar la respuesta despues de un login exitoso.
 * Contiene el token JWT, ID del usuario y otros datos.
 */
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long idUsuario; // Usamos idUsuario de nuestra entidad
    private String nombre; // Usamos nombre de nuestra entidad
    private String email;
    private List<String> roles; // Opcional, si agregamos roles mas tarde

    public JwtResponse(String accessToken, Long idUsuario, String nombre, String email, List<String> roles) {
        this.token = accessToken;
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.email = email;
        this.roles = roles;
    }
    
    // --- Getters y Setters ---

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
