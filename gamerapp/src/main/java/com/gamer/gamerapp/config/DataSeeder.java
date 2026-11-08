package com.gamer.gamerapp.config;

import com.gamer.gamerapp.model.User;
import com.gamer.gamerapp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@gamerapp.com");
            admin.setRoles(Set.of("ADMIN", "USER"));
            
            userRepository.save(admin);
            System.out.println(">>> Usuario de prueba 'admin' creado exitosamente con roles [ADMIN, USER].");
        }


        if (!userRepository.existsByUsername("user")) {
            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setEmail("user@gamerapp.com");
            user.setRoles(Set.of("USER"));
            
            userRepository.save(user);
            System.out.println(">>> Usuario de prueba 'user' creado exitosamente con rol [USER].");
        }


        List<User> usuariosSinRoles = userRepository.findAll().stream()
                .filter(u -> u.getRoles() == null || u.getRoles().isEmpty())
                .collect(Collectors.toList());

        if (!usuariosSinRoles.isEmpty()) {
            for (User u : usuariosSinRoles) {
                u.setRoles(Set.of("USER"));
            }
            userRepository.saveAll(usuariosSinRoles);
            System.out.println(">>> Sanación de datos: Se asignó el rol por defecto 'USER' a " + usuariosSinRoles.size() + " usuario(s) antiguo(s).");
        }
    }
}