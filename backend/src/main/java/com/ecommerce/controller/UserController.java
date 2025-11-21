package com.ecommerce.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/v1/")
public class UserController {


    @GetMapping("/user")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> user() {
        return ResponseEntity.ok("OK" );
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> admin() {
        return ResponseEntity.ok("ADMIN");
    }

    @GetMapping("/public")
    public ResponseEntity<?> publica() {
        return ResponseEntity.ok("Public");
    }
}