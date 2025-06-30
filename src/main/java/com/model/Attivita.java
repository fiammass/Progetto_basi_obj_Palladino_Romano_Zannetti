package com.model;

public class Attivita {
    private String nome;
    private Stato stato;
    private String linkUrl;

    public Attivita(String nome) {
        this.nome = nome;
        this.stato = Stato.NON_COMPLETATO;
    }

    public void completa() {
        this.stato = Stato.COMPLETATO;
    }

    public String getNome() {
        return nome;
    }

    public Stato getStato() {
        return stato;
    }

    @Override
    public String toString() {
        return nome + " - " + stato.getDescrizione();
    }
}