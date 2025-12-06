package com.ecommerce.security.jwt;

import com.ecommerce.security.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro que se ejecuta una vez por cada peticion HTTP.
 * Se encarga de:
 * 1. Extraer el JWT del header de la peticion (Authorization: Bearer <token>).
 * 2. Validar el token.
 * 3. Cargar el usuario y establecer la autenticacion en el SecurityContext.
 */
public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                // Carga los detalles del usuario
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Crea el objeto de autenticacion de Spring Security
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null, // No pasamos la contrasena
                                userDetails.getAuthorities()); // Pasamos los roles/autoridades

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Establece la autenticacion en el contexto de seguridad
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("No se pudo establecer la autenticacion del usuario: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Funcion auxiliar para extraer el JWT del header "Authorization".
     */
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        // Verifica si el header empieza con "Bearer " y extrae el token.
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7); // Retorna la parte despues de "Bearer "
        }

        return null;
    }
}
