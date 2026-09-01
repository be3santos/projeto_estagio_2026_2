package com.boardgamecafe.controller;

import com.boardgamecafe.model.CategoriaJogo;
import com.boardgamecafe.model.Reserva;
import com.boardgamecafe.repository.ReservaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;

@Controller
public class PublicoController {

    @Autowired
    private ReservaRepository reservaRepository;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("reserva", new Reserva());
        model.addAttribute("categorias", CategoriaJogo.values());
        model.addAttribute("hoje", LocalDate.now().toString());
        model.addAttribute("limiteData", LocalDate.now().plusYears(1).toString());
        return "index";
    }

    @PostMapping("/reservar")
    public String reservar(@Valid @ModelAttribute("reserva") Reserva reserva,
                           BindingResult resultado,
                           Model model) {
        if (resultado.hasErrors()) {
            model.addAttribute("categorias", CategoriaJogo.values());
            model.addAttribute("hoje", LocalDate.now().toString());
            model.addAttribute("limiteData", LocalDate.now().plusYears(1).toString());
            return "index";
        }
        reservaRepository.save(reserva);
        model.addAttribute("categorias", CategoriaJogo.values());
        model.addAttribute("hoje", LocalDate.now().toString());
        model.addAttribute("limiteData", LocalDate.now().plusYears(1).toString());
        model.addAttribute("reserva", new Reserva());
        model.addAttribute("sucesso", true);
        model.addAttribute("nomeConfirmado", reserva.getNome());
        return "index";
    }
}