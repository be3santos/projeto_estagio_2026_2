package com.boardgamecafe.controller;

import com.boardgamecafe.model.Reserva;
import com.boardgamecafe.model.StatusReserva;
import com.boardgamecafe.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ReservaRepository reservaRepository;

    @GetMapping("/painel")
    public String painel(@RequestParam(required = false) String status, Model model) {
        List<Reserva> reservas;
        if (status != null && !status.isBlank()) {
            reservas = reservaRepository.findByStatusOrderByDataReservaAscHorarioAsc(StatusReserva.valueOf(status));
        } else {
            reservas = reservaRepository.findAllByOrderByDataReservaAscHorarioAsc();
        }
        model.addAttribute("reservas", reservas);
        model.addAttribute("filtroAtual", status == null ? "" : status);
        model.addAttribute("totalPendentes", reservaRepository.countByStatus(StatusReserva.PENDENTE));
        model.addAttribute("totalConfirmados", reservaRepository.countByStatus(StatusReserva.CONFIRMADO));
        model.addAttribute("totalCancelados", reservaRepository.countByStatus(StatusReserva.CANCELADO));
        return "dashboard";
    }

    @PostMapping("/reservas/{id}/status")
    public String atualizarStatus(@PathVariable Long id, @RequestParam StatusReserva novoStatus) {
        Reserva reserva = reservaRepository.findById(id).orElseThrow();
        reserva.setStatus(novoStatus);
        reservaRepository.save(reserva);
        return "redirect:/admin/painel";
    }
}