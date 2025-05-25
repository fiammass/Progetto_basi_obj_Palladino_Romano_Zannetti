package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Bacheca {
    private String titolo;
    private String descrizione;
    private List<ToDo> todos;

    public Bacheca(String titolo, String descrizione) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.todos = new ArrayList<>();
    }

    public void aggiungiTodo(ToDo todo) {
        todos.add(todo);
    }

    public List<ToDo> getTodosInScadenza(Date dataLimite) {
        return todos.stream()
                .filter(t -> !t.getDataScadenza().after(dataLimite))
                .sorted((t1, t2) -> t1.getDataScadenza().compareTo(t2.getDataScadenza()))
                .collect(Collectors.toList());
    }

    public List<ToDo> cerca(String query) {
        return todos.stream()
                .filter(t -> t.getTitolo().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    public String getTitolo() {
        return titolo;
    }

    public List<ToDo> getTodos() {
        return this.todos;
    }

}