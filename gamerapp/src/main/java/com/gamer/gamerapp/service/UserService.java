package com.gamer.gamerapp.service;

import com.gamer.gamerapp.model.User;
import com.gamer.gamerapp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registrarUsuario(User nuevoUsuario) {

        if (nuevoUsuario == null || 
            nuevoUsuario.getUsername() == null || nuevoUsuario.getUsername().trim().isEmpty() ||
            nuevoUsuario.getPassword() == null || nuevoUsuario.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario y la contraseña son obligatorios.");
        }

        String usernameLimpio = nuevoUsuario.getUsername().trim();

        if (userRepository.existsByUsername(usernameLimpio)) {
            throw new IllegalArgumentException("El nombre de usuario '" + usernameLimpio + "' ya está registrado.");
        }

        if (nuevoUsuario.getEmail() != null && !nuevoUsuario.getEmail().trim().isEmpty()) {
            if (userRepository.existsByEmail(nuevoUsuario.getEmail().trim())) {
                throw new IllegalArgumentException("El correo electrónico ya se encuentra registrado.");
            }
        }

        nuevoUsuario.setUsername(usernameLimpio);
        nuevoUsuario.setPassword(passwordEncoder.encode(nuevoUsuario.getPassword()));
        

        if (nuevoUsuario.getRoles() == null || nuevoUsuario.getRoles().isEmpty()) {
            nuevoUsuario.setRoles(Set.of("USER"));
        }
        
        userRepository.save(nuevoUsuario);
    }
}