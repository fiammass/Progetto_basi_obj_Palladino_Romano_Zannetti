package com.model;

public class Permessi {
    public enum TipoPermesso {
        CREARE, MODIFICARE, ELIMINARE, SPOSTARE, CONDIVIDERE
    }

    private boolean[] permessi;

    public Permessi(boolean creare, boolean modificare, boolean eliminare, boolean spostare, boolean condividere) {
        this.permessi = new boolean[TipoPermesso.values().length];
        this.permessi[TipoPermesso.CREARE.ordinal()] = creare;
        this.permessi[TipoPermesso.MODIFICARE.ordinal()] = modificare;
        this.permessi[TipoPermesso.ELIMINARE.ordinal()] = eliminare;
        this.permessi[TipoPermesso.SPOSTARE.ordinal()] = spostare;
        this.permessi[TipoPermesso.CONDIVIDERE.ordinal()] = condividere;
    }

    public boolean haPermesso(TipoPermesso permesso) {
        return permessi[permesso.ordinal()];
    }
}