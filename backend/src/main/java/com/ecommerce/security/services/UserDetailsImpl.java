package com.ecommerce.security.services;

import com.ecommerce.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;

/**
 * Clase que implementa la interfaz UserDetails de Spring Security.
 * Contiene la informacion de un usuario autenticado y sus autoridades.
 */
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String email;

    // La anotacion @JsonIgnore asegura que la contrasena nunca se serialice en la respuesta
    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;
    
    // Por defecto, asumiremos que todos los usuarios estan activos y con las cuentas no bloqueadas,
    // pero se pueden anadir campos de la entidad User si es necesario (ej. roles).

    public UserDetailsImpl(Long id, String username, String email, String password,
                           Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    /**
     * Metodo factoria para crear una instancia de UserDetailsImpl a partir de la entidad User.
     * En este proyecto, asumimos roles simples, pero se puede extender para incluir roles.
     * @param user La entidad User cargada desde la base de datos.
     * @return Una instancia de UserDetailsImpl.
     */
    public static UserDetailsImpl build(User user) {
        // En este ejemplo simple, no estamos usando roles/GrantedAuthority, 
        // pero la estructura esta lista para agregarlos mas tarde si es necesario.
        // Si no tienes roles definidos, authorities puede ser una lista vacia o nula.
        Collection<? extends GrantedAuthority> authorities = null; 

        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                authorities);
    }

    // --- Getters especificos para la aplicacion ---
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    // --- Implementacion de metodos de la interfaz UserDetails ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // --- Metodos de comparacion ---

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}
