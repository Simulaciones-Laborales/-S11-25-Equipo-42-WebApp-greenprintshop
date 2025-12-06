package com.ecommerce.service;

import com.ecommerce.entity.Usuario;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define las operaciones de negocio para la entidad Usuario.
 * Este servicio separa la logica de negocio del controlador.
 */
public interface UsuarioService {
    
    /**
     * Obtiene una lista de todos los usuarios.
     * @return Una lista de entidades Usuario.
     */
    List<Usuario> findAll();

    /**
     * Busca un usuario por su ID.
     * @param idUsuario El ID del usuario a buscar.
     * @return Un Optional que contiene el Usuario si es encontrado.
     */
    Optional<Usuario> findById(Long idUsuario);

    /**
     * Guarda o actualiza un usuario.
     * @param usuario La entidad Usuario a guardar.
     * @return El Usuario guardado.
     */
    Usuario save(Usuario usuario);

    /**
     * Elimina un usuario por su ID.
     * @param idUsuario El ID del usuario a eliminar.
     */
    void deleteById(Long idUsuario);

    /**
     * Busca un usuario por su nombre (identificador de login).
     * @param nombre El nombre del usuario a buscar.
     * @return Un Optional que contiene el Usuario si es encontrado.
     */
    Optional<Usuario> findByNombre(String nombre);
}
