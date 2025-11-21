package com.ecommerce.dto.usuario.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterUserRequest {

    @NotBlank()
    @Size(min = 3, max = 20)
    private String nombre;

    @NotBlank()
    @Size(max = 50)
    @Email()
    private String email;

    @NotBlank()
    @Size(max = 100)
    private String password;
}