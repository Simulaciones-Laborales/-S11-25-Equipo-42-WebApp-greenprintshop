package com.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ecommerce.model.Usuario;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Usuario, Long> {
   
    Optional<Usuario> findByEmail(String email);
    Boolean existsByEmail(String email);
}