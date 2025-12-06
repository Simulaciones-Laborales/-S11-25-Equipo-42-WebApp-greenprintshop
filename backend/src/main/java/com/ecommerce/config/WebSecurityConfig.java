package com.ecommerce.config;

import com.ecommerce.security.jwt.AuthEntryPointJwt;
import com.ecommerce.security.jwt.AuthTokenFilter;
import com.ecommerce.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Clase de configuracion principal para Spring Security.
 * Habilita la seguridad web y la seguridad a nivel de metodos (con @PreAuthorize).
 */
@Configuration
@EnableWebSecurity // Habilita la integracion de Spring Security
@EnableMethodSecurity(
        // Por defecto, usa seguridad a nivel de metodo, como @PreAuthorize("isAuthenticated()")
        securedEnabled = true, // Permite @Secured
        jsr250Enabled = true // Permite @RolesAllowed
)
public class WebSecurityConfig {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    /**
     * Define el filtro que extraera y validara el JWT en cada peticion.
     * Se define como un Bean para que Spring lo pueda inyectar en la cadena de seguridad.
     */
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    /**
     * Define el proveedor de autenticacion, que utiliza nuestro UserDetailsService
     * y el PasswordEncoder para validar las credenciales.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    /**
     * Bean para el AuthenticationManager, requerido para el proceso de login.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Define el algoritmo de hash para las contraseñas (BCrypt).
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Define la cadena de filtros de seguridad HTTP.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // Deshabilita CSRF (ya que usamos JWT stateless)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler)) // Manejo de errores 401
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Politica de sesion stateless (no usa sesiones HTTP)
                .authorizeHttpRequests(auth -> auth
                        // Permite acceso publico a los endpoints de autenticacion y al contenido publico
                        .requestMatchers("/api/auth/**").permitAll() // /api/auth/login, /api/auth/register
                        .requestMatchers("/api/test/public").permitAll()
                        .anyRequest().authenticated() // Cualquier otra peticion requiere autenticacion (JWT)
                );

        // Agrega nuestro proveedor de autenticacion personalizado
        http.authenticationProvider(authenticationProvider());

        // Agrega el filtro JWT antes del filtro estandar de Spring Security
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
