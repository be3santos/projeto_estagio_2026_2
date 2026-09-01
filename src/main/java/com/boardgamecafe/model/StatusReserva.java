package com.boardgamecafe.model;

public enum StatusReserva {
    PENDENTE("Pendente"),
    CONFIRMADO("Confirmado"),
    CANCELADO("Cancelado");

    private final String rotulo;

    StatusReserva(String rotulo) {
        this.rotulo = rotulo;
    }

    public String getRotulo() {
        return rotulo;
    }
}