package com.gamer.gamerapp.controller;

import com.gamer.gamerapp.dto.ComentarioRequest;
import com.gamer.gamerapp.dto.CompartirRequest;
import com.gamer.gamerapp.dto.MediaRequest;
import com.gamer.gamerapp.model.Evento;
import com.gamer.gamerapp.model.EventoComentario;
import com.gamer.gamerapp.model.EventoMedia;
import com.gamer.gamerapp.service.EventoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/eventos")
public class EventoRestController {

    private final EventoService eventoService;

    public EventoRestController(EventoService eventoService) {
        this.eventoService = eventoService;
    }


    @GetMapping
    public ResponseEntity<List<Evento>> listarEventos() {
        return ResponseEntity.ok(eventoService.listarTodos());
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerEvento(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(eventoService.obtenerPorId(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No encontrado", "message", e.getMessage()));
        }
    }


    @PostMapping("/{id}/media")
    public ResponseEntity<?> agregarMedia(@PathVariable Long id,
                                          @RequestBody(required = false) MediaRequest request,
                                          Authentication authentication) {
        if (request == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Solicitud inválida", "message", "El cuerpo JSON es requerido."));
        }
        try {
            EventoMedia media = eventoService.agregarMedia(
                    id,
                    request.getUrl(),
                    request.getTipo(),
                    request.getDescripcion(),
                    authentication.getName()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(media);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Validación fallida", "message", e.getMessage()));
        }
    }


    @PostMapping("/{id}/comentarios")
    public ResponseEntity<?> agregarComentario(@PathVariable Long id,
                                               @RequestBody(required = false) ComentarioRequest request,
                                               Authentication authentication) {
        if (request == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Solicitud inválida", "message", "El cuerpo JSON es requerido."));
        }
        try {
            EventoComentario comentario = eventoService.agregarComentario(
                    id,
                    request.getContenido(),
                    request.getValoracion(),
                    authentication.getName()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(comentario);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Validación fallida", "message", e.getMessage()));
        }
    }


    @PostMapping("/{id}/compartir")
    public ResponseEntity<?> compartirEvento(@PathVariable Long id,
                                             @RequestBody(required = false) CompartirRequest request,
                                             Authentication authentication) {
        if (request == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Solicitud inválida", "message", "El cuerpo JSON es requerido."));
        }
        try {
            Map<String, Object> resultado = eventoService.compartirEvento(
                    id,
                    request.getPlataforma(),
                    request.getMensaje(),
                    authentication.getName()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Validación fallida", "message", e.getMessage()));
        }
    }
}