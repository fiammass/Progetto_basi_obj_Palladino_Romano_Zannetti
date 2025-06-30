package com.todomanager.gui.components;

import com.todomanager.controller.TodoController;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BoardPanel extends JPanel {
    private final String boardName;
    private final TodoController todoController;

    public BoardPanel(String boardName, TodoController todoController) {
        this.boardName = boardName;
        this.todoController = todoController;
        setupPanel();
    }

    private void setupPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel(boardName, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);

        JPanel todosPanel = new JPanel();
        todosPanel.setLayout(new BoxLayout(todosPanel, BoxLayout.Y_AXIS));

        try {
            List<com.todomanager.model.ToDo> todos = todoController.getTodosByBoard(boardName);
            for (com.todomanager.model.ToDo todo : todos) {
                todosPanel.add(new TodoCard(todo));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Errore nel caricamento: " + e.getMessage());
        }

        add(new JScrollPane(todosPanel), BorderLayout.CENTER);
    }
}