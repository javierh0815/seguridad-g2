package com.gamer.gamerapp.repository;

import com.gamer.gamerapp.model.EventoCompartido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EventoCompartidoRepository extends JpaRepository<EventoCompartido, Long> {
    List<EventoCompartido> findByEventoId(Long eventoId);
}