package com.model;

public class Condivisione {
    private ToDo toDo;
    private Utente proprietario;
    private Utente utenteCondiviso;

    public Condivisione(ToDo toDo, Utente proprietario, Utente utenteCondiviso) {
        this.toDo = toDo;
        this.proprietario = proprietario;
        this.utenteCondiviso = utenteCondiviso;
    }

    // Getter
    public ToDo getToDo() { return toDo; }
    public Utente getProprietario() { return proprietario; }
    public Utente getUtenteCondiviso() { return utenteCondiviso; }

    @Override
    public String toString() {
        return toDo.getTitolo() + " condiviso da " + proprietario.getUsername() +
                " con " + utenteCondiviso.getUsername();
    }
}