package com.boardgamecafe.repository;

import com.boardgamecafe.model.Reserva;
import com.boardgamecafe.model.StatusReserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByEmailIgnoreCaseOrderByDataReservaDescHorarioDesc(String email);

    List<Reserva> findAllByOrderByDataReservaAscHorarioAsc();

    List<Reserva> findByStatusOrderByDataReservaAscHorarioAsc(StatusReserva status);

    long countByStatus(StatusReserva status);

    long countByDataReservaAndHorarioAndStatusNot(LocalDate dataReserva, LocalTime horario, StatusReserva status);
}