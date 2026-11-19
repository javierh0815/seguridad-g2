package com.gamer.gamerapp.repository;

import com.gamer.gamerapp.model.EventoComentario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EventoComentarioRepository extends JpaRepository<EventoComentario, Long> {
    List<EventoComentario> findByEventoId(Long eventoId);
}