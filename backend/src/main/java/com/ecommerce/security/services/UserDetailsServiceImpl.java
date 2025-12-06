package com.ecommerce.security.services;

import com.ecommerce.models.User;
import com.ecommerce.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio principal de Spring Security que implementa UserDetailsService.
 * Carga los detalles del usuario desde la base de datos por su 'username'.
 * Es utilizado por el AuthenticationManager para la autenticacion.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    
    // Inyeccion del repositorio para acceder a la base de datos
    @Autowired
    UserRepository userRepository;

    /**
     * Metodo requerido por la interfaz UserDetailsService.
     * Busca el usuario en la base de datos y lo convierte a un objeto UserDetails.
     *
     * @param username El nombre de usuario que se busca.
     * @return Una instancia de UserDetailsImpl que contiene los datos del usuario.
     * @throws UsernameNotFoundException Si no se encuentra el usuario en el repositorio.
     */
    @Override
    @Transactional // Asegura que toda la operacion de carga se realice en una sola transaccion
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca el usuario por nombre de usuario. Asumimos que UserRepository tiene findByUsername.
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con username: " + username));

        // Transforma la entidad User en el objeto UserDetails que Spring Security necesita
        return UserDetailsImpl.build(user);
    }
}
