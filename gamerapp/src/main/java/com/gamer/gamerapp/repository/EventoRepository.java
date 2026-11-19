package com.gamer.gamerapp.repository;

import com.gamer.gamerapp.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoRepository extends JpaRepository<Evento, Long> {
}