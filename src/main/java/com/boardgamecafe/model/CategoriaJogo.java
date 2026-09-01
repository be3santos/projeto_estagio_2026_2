package com.boardgamecafe.model;

public enum CategoriaJogo {
    ESTRATEGIA("Estratégia"),
    FAMILIA("Família"),
    COOPERATIVO("Cooperativo"),
    PARTY("Party Game");

    private final String rotulo;

    CategoriaJogo(String rotulo) {
        this.rotulo = rotulo;
    }

    public String getRotulo() {
        return rotulo;
    }
}