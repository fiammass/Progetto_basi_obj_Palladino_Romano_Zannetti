package gui.components;

import controller.TodoController;
import model.Todo;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class BoardPanel extends JPanel {
    private final String boardName;
    private final TodoController todoController;
    private JPanel todosPanel;

    public BoardPanel(String boardName, TodoController todoController) {
        this.boardName = boardName;
        this.todoController = todoController;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header
        JLabel titleLabel = new JLabel(boardName, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);

        // Todos container
        todosPanel = new JPanel();
        todosPanel.setLayout(new BoxLayout(todosPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(todosPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void addTodoCard(Todo todo) {
        TodoCard card = new TodoCard(todo, todoController);
        todosPanel.add(card);
        todosPanel.revalidate();
    }

    public void refreshTodos() {
        todosPanel.removeAll();
        try {
            List<Todo> todos = todoController.getTodosByBoard(boardName);
            for (Todo todo : todos) {
                addTodoCard(todo);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Errore nel refresh: " + e.getMessage());
        }
        revalidate();
        repaint();
    }
}