package com.gamer.gamerapp.repository;

import com.gamer.gamerapp.model.EventoMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EventoMediaRepository extends JpaRepository<EventoMedia, Long> {
    List<EventoMedia> findByEventoId(Long eventoId);
}