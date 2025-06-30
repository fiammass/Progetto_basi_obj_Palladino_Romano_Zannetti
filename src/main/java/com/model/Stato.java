package com.model;

public enum Stato {
    IN_CORSO("In corso"),
    COMPLETATO("Completato"),
    NON_COMPLETATO("Non completato"),
    SCADUTO("Scaduto");

    private final String descrizione;

    Stato(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getDescrizione() {
        return descrizione;
    }
}