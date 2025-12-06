package com.ecommerce.security.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Maneja el error de autenticacion 401 (Unauthorized).
 * Se dispara cuando un usuario no autenticado intenta acceder a un recurso protegido.
 */
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        
        logger.error("Error No Autorizado: {}", authException.getMessage());
        
        // Establece el codigo de estado HTTP 401 (Unauthorized)
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: No Autorizado");
    }
}
