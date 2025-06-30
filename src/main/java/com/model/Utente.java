package com.model;

import java.util.ArrayList;
import java.util.List;

public class Utente {
    private String username;
    private String password;
    private Permessi permessi;
    private List<Bacheca> bacheche;
    private List<ToDo> todosCondivisi;

    public Utente(String username, String password) {
        this.username = username;
        this.password = password;
        this.bacheche = new ArrayList<>();
        this.todosCondivisi = new ArrayList<>();
        inizializzaBacheche();
    }

    private void inizializzaBacheche() {
        bacheche.add(new Bacheca("Università", "Attività accademiche"));
        bacheche.add(new Bacheca("Lavoro", "Attività professionali"));
        bacheche.add(new Bacheca("Tempo Libero", "Attività personali"));
    }

    public void aggiungiTodoCondiviso(ToDo todo) {
        todosCondivisi.add(todo);
    }

    public Bacheca getBacheca(String tipo) {
        return bacheche.stream()
                .filter(b -> b.getTitolo().equalsIgnoreCase(tipo))
                .findFirst()
                .orElse(null);
    }

    public void assegnaPermessi(Permessi permessi) {
        this.permessi = permessi;
    }

    public String getUsername() {
        return username;
    }

    public List<Bacheca> getBacheche() {
        return this.bacheche;
    }

}