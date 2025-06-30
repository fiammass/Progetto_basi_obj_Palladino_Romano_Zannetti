package gui.components;

import controller.TodoController;
import model.Todo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

public class TodoCard extends JPanel {
    private final Todo todo;
    private final TodoController todoController;

    public TodoCard(Todo todo, TodoController todoController) {
        this.todo = todo;
        this.todoController = todoController;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        // Top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel(todo.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel dueLabel = new JLabel("Scade: " + todo.getDueDate());
        if (isExpired(todo.getDueDate())) {
            dueLabel.setForeground(Color.RED);
        }

        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(dueLabel, BorderLayout.EAST);

        // Center panel
        JTextArea descArea = new JTextArea(todo.getDescription());
        descArea.setEditable(false);
        descArea.setLineWrap(true);
        descArea.setBackground(getBackground());

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton editButton = new JButton("Modifica");
        JButton deleteButton = new JButton("Elimina");

        editButton.addActionListener(this::handleEdit);
        deleteButton.addActionListener(this::handleDelete);

        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        add(topPanel, BorderLayout.NORTH);
        add(descArea, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private boolean isExpired(String dueDate) {
        // Implementa la logica di controllo scadenza
        return false; // Sostituire con implementazione reale
    }

    private void handleEdit(ActionEvent e) {
        // Implementa la modifica
    }

    private void handleDelete(ActionEvent e) {
        try {
            todoController.deleteTodo(todo.getId());
            ((BoardPanel) getParent().getParent().getParent()).refreshTodos();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Errore nell'eliminazione");
        }
    }
}