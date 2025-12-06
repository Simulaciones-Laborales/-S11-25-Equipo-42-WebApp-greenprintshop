package com.ecommerce.service;

import com.ecommerce.entity.Usuario;
import com.ecommerce.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementacion del servicio de logica de negocio para la entidad Usuario.
 * Utiliza UserRepository para la interaccion con la base de datos.
 */
@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findById(Long idUsuario) {
        return userRepository.findById(idUsuario);
    }

    @Override
    @Transactional
    public Usuario save(Usuario usuario) {
        // En un escenario real, aqui se podrian incluir validaciones adicionales o logica de negocio
        return userRepository.save(usuario);
    }

    @Override
    @Transactional
    public void deleteById(Long idUsuario) {
        userRepository.deleteById(idUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findByNombre(String nombre) {
        return userRepository.findByNombre(nombre);
    }
}
