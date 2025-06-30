package com.model;

import java.text.SimpleDateFormat;

public class Main {
    public static void main(String[] args) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        // 1. Creazione utente
        Utente utente = new Utente("mario", "password");
        utente.assegnaPermessi(new Permessi(true, true, true, true, true));

        // 2. Creazione e aggiunta ToDo
        Bacheca lavoro = utente.getBacheca("Lavoro");
        ToDo todo = new ToDo("Consegnare progetto", sdf.parse("30/06/2023"));
        lavoro.aggiungiTodo(todo);

        // 3. Aggiunta attività
        todo.aggiungiAttivita("Scrivere documentazione");
        todo.aggiungiAttivita("Testare il codice");


        // 4. Completamento attività
        todo.completaAttivita("Scrivere documentazione");

        // 5. Stampa stato
        System.out.println("Stato ToDo: " + todo);

        todo.mostraAttivita();
    }
}