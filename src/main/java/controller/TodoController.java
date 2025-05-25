package controller;

import model.*;
import java.util.*;
import java.util.stream.Collectors;

public class TodoController {
    private final Utente currentUser;
    private Bacheca bachecaCorrente;

    public TodoController() {
        this.currentUser = new Utente("admin", "password");
        this.currentUser.assegnaPermessi(new Permessi(true, true, true, true, true));
        this.selezionaBacheca("Lavoro"); // Imposta bacheca iniziale di default
    }

    // METODI PER BACHECHE
    public void selezionaBacheca(String nomeBacheca) {
        this.bachecaCorrente = currentUser.getBacheca(nomeBacheca);
    }

    public List<String> getNomiBacheche() {
        return currentUser.getBacheche().stream()
                .map(Bacheca::getTitolo)
                .collect(Collectors.toList());
    }

    public String getBachecaCorrenteNome() {
        return bachecaCorrente != null ? bachecaCorrente.getTitolo() : "";
    }

    // METODI PER TODO
    public boolean aggiungiTodo(String titolo, Date scadenza) {
        if (bachecaCorrente == null) return false;

        ToDo nuovoTodo = new ToDo(titolo, scadenza);
        bachecaCorrente.aggiungiTodo(nuovoTodo);
        return true;
    }

    public boolean rimuoviTodo(String titolo) {
        if (bachecaCorrente == null) return false;

        return bachecaCorrente.getTodos().removeIf(t -> t.getTitolo().equals(titolo));
    }

    // METODI PER ATTIVITÀ
    public boolean aggiungiAttivita(String todoTitolo, String attivitaNome) {
        if (bachecaCorrente == null) return false;

        return bachecaCorrente.getTodos().stream()
                .filter(t -> t.getTitolo().equals(todoTitolo))
                .findFirst()
                .map(t -> {
                    t.aggiungiAttivita(attivitaNome);
                    return true;
                })
                .orElse(false);
    }

    public boolean completaAttivita(String todoTitolo, String attivitaNome) {
        if (bachecaCorrente == null) return false;

        return bachecaCorrente.getTodos().stream()
                .filter(t -> t.getTitolo().equals(todoTitolo))
                .findFirst()
                .map(t -> {
                    t.completaAttivita(attivitaNome);
                    return true;
                })
                .orElse(false);
    }

    // METODI DI RICERCA
    public List<ToDo> getTodosInScadenza(Date dataLimite) {
        if (bachecaCorrente == null) return Collections.emptyList();
        return bachecaCorrente.getTodosInScadenza(dataLimite);
    }

    public List<ToDo> cercaTodo(String query) {
        if (bachecaCorrente == null) return Collections.emptyList();
        return bachecaCorrente.cerca(query);
    }

    public List<ToDo> getTodosBachecaCorrente() {
        if (bachecaCorrente == null) return Collections.emptyList();
        return new ArrayList<>(bachecaCorrente.getTodos());
    }

    // METODI PER CONDIVISIONE
    public boolean condividiTodo(String todoTitolo, Utente altroUtente) {
        if (bachecaCorrente == null) return false;

        return bachecaCorrente.getTodos().stream()
                .filter(t -> t.getTitolo().equals(todoTitolo))
                .findFirst()
                .map(t -> {
                    t.condividiCon(altroUtente);
                    return true;
                })
                .orElse(false);
    }
}