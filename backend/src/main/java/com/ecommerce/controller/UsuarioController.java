package com.ecommerce.controller;

import com.ecommerce.entity.Usuario;
import com.ecommerce.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestion de la entidad Usuario.
 * Nota: Los endpoints de login y registro ya estan en AuthController.
 * Este controlador maneja el CRUD (Crear, Leer, Actualizar, Eliminar) general.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Obtiene todos los usuarios del sistema.
     * En un sistema real, este endpoint deberia estar protegido con @PreAuthorize("hasRole('ADMIN')").
     * @return Una lista de usuarios.
     */
    @GetMapping
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        List<Usuario> usuarios = usuarioService.findAll();
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Obtiene un usuario por su ID.
     * @param idUsuario ID del usuario a buscar.
     * @return El usuario si existe, o un 404.
     */
    @GetMapping("/{idUsuario}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Long idUsuario) {
        return usuarioService.findById(idUsuario)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Crea un nuevo usuario o actualiza uno existente.
     * Nota: Para un caso de uso mas simple, se utiliza la misma entidad Usuario. 
     * En produccion, se recomendaria usar DTOs de peticion (Request DTOs).
     * @param usuario La entidad Usuario a guardar o actualizar.
     * @return El usuario guardado con el estado HTTP 201 (Creado).
     */
    @PostMapping
    public ResponseEntity<Usuario> createOrUpdateUsuario(@RequestBody Usuario usuario) {
        Usuario savedUsuario = usuarioService.save(usuario);
        return new ResponseEntity<>(savedUsuario, HttpStatus.CREATED);
    }

    /**
     * Elimina un usuario por su ID.
     * @param idUsuario ID del usuario a eliminar.
     * @return Respuesta 204 No Content.
     */
    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long idUsuario) {
        usuarioService.deleteById(idUsuario);
        return ResponseEntity.noContent().build();
    }
}
