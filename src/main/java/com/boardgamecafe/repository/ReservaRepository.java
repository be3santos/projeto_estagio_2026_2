package com.boardgamecafe.repository;

import com.boardgamecafe.model.Reserva;
import com.boardgamecafe.model.StatusReserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findAllByOrderByDataReservaAscHorarioAsc();

    List<Reserva> findByStatusOrderByDataReservaAscHorarioAsc(StatusReserva status);

    long countByStatus(StatusReserva status);
}