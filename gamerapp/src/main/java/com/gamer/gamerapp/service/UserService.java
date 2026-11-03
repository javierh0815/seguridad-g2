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

        nuevoUsuario.setPassword(passwordEncoder.encode(nuevoUsuario.getPassword()));
        
        if (nuevoUsuario.getRoles() == null || nuevoUsuario.getRoles().isEmpty()) {
            nuevoUsuario.setRoles(Set.of("USER"));
        }
        

        userRepository.save(nuevoUsuario);
    }
}