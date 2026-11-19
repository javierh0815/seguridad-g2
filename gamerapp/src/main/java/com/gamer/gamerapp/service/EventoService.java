package com.gamer.gamerapp.service;

import com.gamer.gamerapp.model.*;
import com.gamer.gamerapp.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;
    private final EventoMediaRepository mediaRepository;
    private final EventoComentarioRepository comentarioRepository;
    private final EventoCompartidoRepository compartidoRepository;
    private final UserRepository userRepository;

    public EventoService(EventoRepository eventoRepository,
                         EventoMediaRepository mediaRepository,
                         EventoComentarioRepository comentarioRepository,
                         EventoCompartidoRepository compartidoRepository,
                         UserRepository userRepository) {
        this.eventoRepository = eventoRepository;
        this.mediaRepository = mediaRepository;
        this.comentarioRepository = comentarioRepository;
        this.compartidoRepository = compartidoRepository;
        this.userRepository = userRepository;
    }

    public List<Evento> listarTodos() {
        return eventoRepository.findAll();
    }

    public Evento obtenerPorId(Long id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evento no encontrado con ID: " + id));
    }

    private User obtenerUsuario(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + username));
    }

    @Transactional
    public EventoMedia agregarMedia(Long eventoId, String url, String tipo, String descripcion, String username) {
        if (url == null || url.trim().isEmpty()) {
            throw new IllegalArgumentException("La URL del archivo multimedia es obligatoria.");
        }
        String urlLimpia = url.trim();
        if (!urlLimpia.startsWith("http://") && !urlLimpia.startsWith("https://")) {
            throw new IllegalArgumentException("La URL debe comenzar con http:// o https://");
        }

        String tipoNormalizado = (tipo != null) ? tipo.trim().toUpperCase() : "";
        if (!tipoNormalizado.equals("FOTO") && !tipoNormalizado.equals("VIDEO")) {
            throw new IllegalArgumentException("El tipo de media debe ser 'FOTO' o 'VIDEO'.");
        }

        Evento evento = obtenerPorId(eventoId);
        User usuario = obtenerUsuario(username);

        EventoMedia media = new EventoMedia();
        media.setUrl(urlLimpia);
        media.setTipo(tipoNormalizado);
        media.setDescripcion(descripcion != null ? descripcion.trim() : "");
        media.setEvento(evento);
        media.setUsuario(usuario);

        return mediaRepository.save(media);
    }

    @Transactional
    public EventoComentario agregarComentario(Long eventoId, String contenido, Integer valoracion, String username) {
        if (contenido == null || contenido.trim().isEmpty()) {
            throw new IllegalArgumentException("El contenido del comentario es obligatorio.");
        }
        if (valoracion == null || valoracion < 1 || valoracion > 5) {
            throw new IllegalArgumentException("La valoración debe ser un número entero entre 1 y 5.");
        }

        Evento evento = obtenerPorId(eventoId);
        User usuario = obtenerUsuario(username);

        EventoComentario comentario = new EventoComentario();
        comentario.setContenido(contenido.trim());
        comentario.setValoracion(valoracion);
        comentario.setEvento(evento);
        comentario.setUsuario(usuario);

        return comentarioRepository.save(comentario);
    }

    @Transactional
    public Map<String, Object> compartirEvento(Long eventoId, String plataforma, String mensaje, String username) {
        if (plataforma == null || plataforma.trim().isEmpty()) {
            throw new IllegalArgumentException("La plataforma es obligatoria (WEB, TWITTER, FACEBOOK, WHATSAPP, DISCORD).");
        }

        String plat = plataforma.trim().toUpperCase();
        List<String> permitidas = List.of("WEB", "TWITTER", "FACEBOOK", "WHATSAPP", "DISCORD");
        if (!permitidas.contains(plat)) {
            throw new IllegalArgumentException("Plataforma no soportada. Opciones válidas: " + permitidas);
        }

        Evento evento = obtenerPorId(eventoId);
        User usuario = obtenerUsuario(username);

        EventoCompartido compartido = new EventoCompartido();
        compartido.setPlataforma(plat);
        compartido.setMensaje(mensaje != null && !mensaje.trim().isEmpty()
                ? mensaje.trim()
                : "¡Mira este evento gamer: " + evento.getTitulo() + "!");
        compartido.setEvento(evento);
        compartido.setUsuario(usuario);

        EventoCompartido guardado = compartidoRepository.save(compartido);


        String textoCodificado = URLEncoder.encode(guardado.getMensaje(), StandardCharsets.UTF_8);
        String urlEvento = URLEncoder.encode("https://localhost:8443/eventos", StandardCharsets.UTF_8);

        String enlaceRedSocial = switch (plat) {
            case "TWITTER" -> "https://twitter.com/intent/tweet?text=" + textoCodificado + "&url=" + urlEvento;
            case "FACEBOOK" -> "https://www.facebook.com/sharer/sharer.php?u=" + urlEvento;
            case "WHATSAPP" -> "https://api.whatsapp.com/send?text=" + textoCodificado + "%20" + urlEvento;
            case "DISCORD" -> "https://discord.com/channels/@me";
            default -> "https://localhost:8443/eventos";
        };

        return Map.of(
                "idCompartido", guardado.getId(),
                "evento", evento.getTitulo(),
                "plataforma", plat,
                "mensaje", guardado.getMensaje(),
                "enlaceCompartir", enlaceRedSocial,
                "usuario", usuario.getUsername()
        );
    }
}