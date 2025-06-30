package com.todomanager.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Modello per rappresentare un'attività (ToDo)
 */
public class ToDo {
    private int id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private String board; // "Università", "Lavoro" o "Tempo Libero"
    private boolean completed;
    private String color; // Codice esadecimale (es. "#FF0000")
    private String imageUrl;
    private String linkUrl; // URL correlata all'attività

    // Formattatore per le date
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Costruttori
    public ToDo() {
        this.completed = false;
        this.color = "#FFFFFF"; // Bianco come default
    }

    public ToDo(String title, String board) {
        this();
        this.title = title;
        this.board = board;
    }

    // Metodi di business logic
    public boolean isExpired() {
        return dueDate != null && LocalDate.now().isAfter(dueDate);
    }

    public String getFormattedDueDate() {
        return dueDate != null ? dueDate.format(DATE_FORMATTER) : "Nessuna scadenza";
    }

    // Getters & Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    // Metodo per impostare la data da stringa (dd/MM/yyyy)
    public void setDueDate(String dateString) {
        if (dateString != null && !dateString.trim().isEmpty()) {
            this.dueDate = LocalDate.parse(dateString, DATE_FORMATTER);
        }
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    // Override utili
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        com.todomanager.model.ToDo todo = (com.todomanager.model.ToDo) o;
        return id == todo.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", board='" + board + '\'' +
                ", dueDate=" + getFormattedDueDate() +
                ", completed=" + completed +
                '}';
    }
}