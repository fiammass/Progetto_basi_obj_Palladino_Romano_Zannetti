package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Todo {
    private int id;
    private String title;
    private String description;
    private String dueDate; // Formato "dd/MM/yyyy"
    private String board;
    private boolean completed;
    private String color; // Codice esadecimale (es. "#FF0000")
    private String imageUrl;

    // Costruttori
    public Todo() {}

    public Todo(String title, String description, String board) {
        this.title = title;
        this.description = description;
        this.board = board;
        this.completed = false;
    }

    // Metodi di business logic
    public boolean isExpired() {
        if (dueDate == null || dueDate.isEmpty()) return false;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate due = LocalDate.parse(dueDate, formatter);
        return LocalDate.now().isAfter(due);
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }

    public String getBoard() { return board; }
    public void setBoard(String board) { this.board = board; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", board='" + board + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", completed=" + completed +
                '}';
    }
}