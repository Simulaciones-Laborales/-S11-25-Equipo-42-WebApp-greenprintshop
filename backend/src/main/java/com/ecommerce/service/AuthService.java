package com.ecommerce.service;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ecommerce.config.security.jwt.JwtUtils;
import com.ecommerce.config.security.services.UserDetailsImpl;
import com.ecommerce.dto.Response;
import com.ecommerce.dto.usuario.auth.LoginRequest;
import com.ecommerce.dto.usuario.auth.LoginResponse;
import com.ecommerce.dto.usuario.auth.RegisterUserRequest;
import com.ecommerce.dto.usuario.auth.RegisterUserResponse;
import com.ecommerce.dto.usuario.auth.UserResponse;
import com.ecommerce.model.Usuario;
import com.ecommerce.repository.UserRepository;

import java.util.List;

@Service
public class AuthService {

        public AuthService(
                        UserRepository userRepository,
                        PasswordEncoder encoder,
                        AuthenticationManager authenticationManager,
                        JwtUtils jwtUtils) {
                this.userRepository = userRepository;
                this.encoder = encoder;
                this.authenticationManager = authenticationManager;
                this.jwtUtils = jwtUtils;
        }

        UserRepository userRepository;
        PasswordEncoder encoder;
        AuthenticationManager authenticationManager;
        JwtUtils jwtUtils;

        @Transactional
        public Response<Object> register(RegisterUserRequest request) {

                if (userRepository.existsByEmail(request.getEmail())) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");
                }

                String hashedPassword = encoder.encode(request.getPassword());

                Usuario user = new Usuario();
                user.setNombre(request.getNombre());
                user.setEmail(request.getEmail());
                user.setPassword(hashedPassword);
                user.setIsActive(true);
                user.setRol("ROLE_USER");

                try {
                        userRepository.save(user);
                } catch (Exception e) {
                        throw new RuntimeException("Error al guardar usuario: " + e.getMessage());
                }
                
                RegisterUserResponse registerUserResponse = RegisterUserResponse.builder()
                                .name(user.getNombre())
                                .email(user.getEmail())
                                .build();

                return Response.builder()
                                .responseCode(200)
                                .responseMessage("SUCCESS")
                                .data(registerUserResponse)
                                .build();
        }

        // Login Function
        @Transactional
        public Response<Object> login(LoginRequest request, HttpServletResponse response) {

                // Check if User by Email exist. if not throw error
                userRepository.findByEmail(request.getEmail())
                                .orElseThrow(() -> new RuntimeException("User not found. Please register first"));

                Authentication authentication = authenticationManager
                                .authenticate(
                                        new UsernamePasswordAuthenticationToken(
                                                request.getEmail(),
                                                request.getPassword()
                                        )
                                );

                SecurityContextHolder.getContext().setAuthentication(authentication);

                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

                String jwt = jwtUtils.generateJwtToken(userDetails);

                List<String> roles = userDetails.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .toList();

                LoginResponse loginResponse = LoginResponse.builder()
                                .nombre(userDetails.getUsername())
                                .email(userDetails.getEmail())
                                .roles(roles)
                                .accessToken(jwt)
                                .tokenType("Bearer")
                                .build();

                return Response.builder()
                                .responseCode(200)
                                .responseMessage("SUCCESS")
                                .data(loginResponse)
                                .build();
        }

        // GET USER THAT CURRENTLY LOGIN
        @Transactional
        public Response<Object> getUser() {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
                Long userId = userDetails.getId();

                Usuario user = userRepository.findById(userId).orElseThrow(
                                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not found !"));

                UserResponse userResponse = UserResponse.builder()
                                .id(user.getIdUsuario())
                                .username(user.getNombre())
                                .email(user.getEmail())
                                .isActive(user.getIsActive())
                                // .roles(user.getRoles().stream().map(Role::getName).toList())
                                .build();

                return Response.builder()
                                .responseCode(200)
                                .responseMessage("SUCCESS")
                                .data(userResponse)
                                .build();
        }
}