package com.ecommerce.controller;

import com.ecommerce.dto.JwtResponse;
import com.ecommerce.dto.LoginRequest;
import com.ecommerce.dto.SignupRequest;
import com.ecommerce.entity.Usuario;
import com.ecommerce.repositories.UserRepository;
import com.ecommerce.security.jwt.JwtUtils;
import com.ecommerce.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST para manejar las peticiones de autenticacion (login y registro).
 * Rutas: /api/auth/signin y /api/auth/signup.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    /**
     * Endpoint para manejar el inicio de sesion.
     * @param loginRequest DTO con el nombre y la contrasena.
     * @return ResponseEntity con el token JWT y los datos del usuario.
     */
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        // 1. Autenticar las credenciales
        // Crea un objeto Authentication usando el nombre (username) y la contrasena.
        // El AuthenticationManager utiliza el UserDetailsServiceImpl para cargar el usuario
        // y el PasswordEncoder para comparar las contrasenas.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getNombre(), loginRequest.getPassword()));

        // 2. Establecer la autenticacion en el contexto de seguridad
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Generar el Token JWT
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        // 4. Obtener los detalles del usuario autenticado
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // 5. Preparar los roles para la respuesta (actualmente vacio, pero listo para futuro)
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        
        if (roles.isEmpty()) {
            roles = Collections.emptyList();
        }

        // 6. Devolver la respuesta al cliente
        return ResponseEntity.ok(new JwtResponse(
                jwt,
                userDetails.getIdUsuario(),
                userDetails.getUsername(), // getUsername() retorna el 'nombre'
                userDetails.getEmail(),
                roles));
    }

    /**
     * Endpoint para manejar el registro de un nuevo usuario.
     * @param signUpRequest DTO con los datos del nuevo usuario.
     * @return ResponseEntity con un mensaje de exito.
     */
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        
        // 1. Verificar si el nombre de usuario ya existe
        if (userRepository.existsByNombre(signUpRequest.getNombre())) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: El nombre de usuario ya esta en uso!");
        }

        // 2. Verificar si el email ya existe
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: El email ya esta en uso!");
        }

        // 3. Crear la nueva entidad Usuario
        Usuario usuario = new Usuario(
                signUpRequest.getNombre(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword())); // ¡IMPORTANTE! Codificar la contrasena

        // 4. Guardar el usuario en la base de datos
        userRepository.save(usuario);

        // 5. Devolver mensaje de exito
        return ResponseEntity.ok("Usuario registrado exitosamente!");
    }
}
