package com.gamer.gamerapp.config;

import com.gamer.gamerapp.model.Evento;
import com.gamer.gamerapp.model.User;
import com.gamer.gamerapp.repository.EventoRepository;
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
    private final EventoRepository eventoRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository,
                      EventoRepository eventoRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.eventoRepository = eventoRepository;
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
        }

        if (!userRepository.existsByUsername("user")) {
            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setEmail("user@gamerapp.com");
            user.setRoles(Set.of("USER"));
            userRepository.save(user);
        }

        if (!userRepository.existsByUsername("gamer2")) {
            User gamer2 = new User();
            gamer2.setUsername("gamer2");
            gamer2.setPassword(passwordEncoder.encode("gamer123"));
            gamer2.setEmail("gamer2@gamerapp.com");
            gamer2.setRoles(Set.of("USER"));
            userRepository.save(gamer2);
        }

        List<User> usuariosSinRoles = userRepository.findAll().stream()
                .filter(u -> u.getRoles() == null || u.getRoles().isEmpty())
                .collect(Collectors.toList());

        if (!usuariosSinRoles.isEmpty()) {
            for (User u : usuariosSinRoles) {
                u.setRoles(Set.of("USER"));
            }
            userRepository.saveAll(usuariosSinRoles);
        }

        if (eventoRepository.count() == 0) {
            Evento ev1 = new Evento();
            ev1.setTitulo("Gran Torneo Nacional Valorant");
            ev1.setModalidad("Presencial");
            ev1.setDescripcion("Competencia presencial con los 8 mejores equipos de la región compitiendo por un pozo de $5.000.000 CLP.");
            ev1.setVideojuego("Valorant (Tactical Shooter 5v5)");
            ev1.setFechaHora("15 de Noviembre, 14:00 hrs.");
            ev1.setUbicacionOEnlace("Movistar Arena, Santiago");
            ev1.setOrganizador("GamerCommunity & Riot Games");
            ev1.setRequisitos("Rango mínimo Inmortal, equipo completo de 5 jugadores + 1 suplente, mayor de 16 años.");
            ev1.setInscripcion("Formulario abierto hasta el 10 de Noviembre. Cuota de inscripción gratuita.");
            ev1.setImagenPortada("https://images.unsplash.com/photo-1542751371-adc38448a05e?auto=format&fit=crop&w=600&q=80");

            Evento ev2 = new Evento();
            ev2.setTitulo("LAN Party & Showmatch Online");
            ev2.setModalidad("Online");
            ev2.setDescripcion("Transmisión masiva de partidas comunitarias, torneos de PC en vivo y exhibición de hardware de última generación.");
            ev2.setVideojuego("League of Legends / CS2");
            ev2.setFechaHora("22 de Noviembre, 10:00 hrs.");
            ev2.setUbicacionOEnlace("https://twitch.tv/gamercommunity_official");
            ev2.setOrganizador("Comunidad HardUser");
            ev2.setRequisitos("Cuenta verificada en Discord y registro previo en la plataforma.");
            ev2.setInscripcion("Cupos limitados a 128 participantes individuales.");
            ev2.setImagenPortada("https://images.unsplash.com/photo-1511512578047-dfb367046420?auto=format&fit=crop&w=600&q=80");

            eventoRepository.saveAll(List.of(ev1, ev2));
            System.out.println(">>> Eventos iniciales cargados en MySQL.");
        }
    }
}