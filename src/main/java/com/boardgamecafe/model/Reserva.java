package com.boardgamecafe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "reservas")
public class Reserva {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CategoriaJogo getCategoriaJogo() {
        return categoriaJogo;
    }

    public void setCategoriaJogo(CategoriaJogo categoriaJogo) {
        this.categoriaJogo = categoriaJogo;
    }

    public String getJogoEscolhido() {
        return jogoEscolhido;
    }

    public void setJogoEscolhido(String jogoEscolhido) {
        this.jogoEscolhido = jogoEscolhido;
    }

    public Integer getNumeroPessoas() {
        return numeroPessoas;
    }

    public void setNumeroPessoas(Integer numeroPessoas) {
        this.numeroPessoas = numeroPessoas;
    }

    public LocalDate getDataReserva() {
        return dataReserva;
    }

    public void setDataReserva(LocalDate dataReserva) {
        this.dataReserva = dataReserva;
    }

    public LocalTime getHorario() {
        return horario;
    }

    public void setHorario(LocalTime horario) {
        this.horario = horario;
    }

    public StatusReserva getStatus() {
        return status;
    }

    public void setStatus(StatusReserva status) {
        this.status = status;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Informe seu nome")
    private String nome;

    @NotBlank(message = "Informe seu email")
    @Email(message = "Email inválido")
    private String email;

    @NotNull(message = "Escolha uma categoria de jogo")
    @Enumerated(EnumType.STRING)
    private CategoriaJogo categoriaJogo;

    private String jogoEscolhido;

    @NotNull(message = "Informe o número de pessoas")
    @Min(value = 1, message = "Mínimo de 1 pessoa")
    @Max(value = 12, message = "Máximo de 12 pessoas por mesa")
    private Integer numeroPessoas;

    @NotNull(message = "Escolha uma data")
    @FutureOrPresent(message = "A data precisa ser hoje ou no futuro")
    private LocalDate dataReserva;

    @NotNull(message = "Escolha um horário")
    private LocalTime horario;

    @Enumerated(EnumType.STRING)
    private StatusReserva status = StatusReserva.PENDENTE;

    private LocalDateTime criadoEm = LocalDateTime.now();
}