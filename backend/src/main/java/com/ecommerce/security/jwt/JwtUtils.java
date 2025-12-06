package com.ecommerce.security.jwt;

import com.ecommerce.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Clase de utilidad para crear, validar y procesar el token JWT.
 * Utiliza las librerias io.jsonwebtoken.
 */
@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    // Clave secreta cargada desde application.properties (ej: 'ecommerce.app.jwtSecret')
    // ¡Asegurate de definir esta clave y el tiempo de expiracion en tu archivo de propiedades!
    @Value("${ecommerce.app.jwtSecret}")
    private String jwtSecret;

    // Tiempo de expiracion en milisegundos cargado desde application.properties (ej: 'ecommerce.app.jwtExpirationMs')
    @Value("${ecommerce.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    /**
     * Genera un token JWT a partir de la informacion de autenticacion.
     * Se usa en el proceso de login.
     * @param authentication El objeto de autenticacion de Spring Security.
     * @return El token JWT generado como String.
     */
    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername())) // Usamos el username como sujeto del token
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS512) // Firma el token con la clave secreta
                .compact();
    }

    /**
     * Devuelve la clave HMAC para la firma JWT.
     */
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    /**
     * Extrae el nombre de usuario (username) del token JWT.
     * @param token El token JWT.
     * @return El username contenido en el 'subject' del token.
     */
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Valida la estructura y firma del token JWT.
     * @param authToken El token JWT a validar.
     * @return true si es valido, false si falla la validacion.
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException e) {
            logger.error("Firma JWT invalida: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Token JWT mal formado: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("Token JWT expirado: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Token JWT no soportado: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("Cadena de claims JWT vacia: {}", e.getMessage());
        }

        return false;
    }
}
