package gui;

import controller.TodoController;
import gui.components.BoardPanel;
import model.Todo;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class MainWindow extends JFrame {
    private final TodoController todoController;
    private JTabbedPane tabbedPane;

    public MainWindow(TodoController todoController) {
        this.todoController = todoController;
        initializeUI();
        loadDefaultBoards();
    }

    private void initializeUI() {
        setTitle("ToDo Manager");
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Toolbar
        JToolBar toolBar = new JToolBar();
        JButton addButton = new JButton("+ Nuovo Todo");
        addButton.addActionListener(e -> showAddTodoDialog());
        toolBar.add(addButton);

        // TabbedPane per le bacheche
        tabbedPane = new JTabbedPane();

        add(toolBar, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }

    private void loadDefaultBoards() {
        String[] defaultBoards = {"Università", "Lavoro", "Tempo Libero"};

        for (String boardName : defaultBoards) {
            try {
                BoardPanel panel = new BoardPanel(boardName, todoController);
                loadTodosToPanel(panel, boardName);
                tabbedPane.addTab(boardName, panel);
            } catch (SQLException e) {
                showError("Errore nel caricamento di " + boardName + ": " + e.getMessage());
            }
        }
    }

    private void loadTodosToPanel(BoardPanel panel, String boardName) throws SQLException {
        List<Todo> todos = todoController.getTodosByBoard(boardName);
        for (Todo todo : todos) {
            panel.addTodoCard(todo);
        }
    }

    private void showAddTodoDialog() {
        JDialog dialog = new JDialog(this, "Nuovo Todo", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 300);

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField titleField = new JTextField();
        JTextArea descField = new JTextArea();
        JTextField dueDateField = new JTextField();
        JComboBox<String> boardCombo = new JComboBox<>(new String[]{"Università", "Lavoro", "Tempo Libero"});

        formPanel.add(new JLabel("Titolo*:"));
        formPanel.add(titleField);
        formPanel.add(new JLabel("Descrizione:"));
        formPanel.add(new JScrollPane(descField));
        formPanel.add(new JLabel("Scadenza (gg/mm/aaaa):"));
        formPanel.add(dueDateField);
        formPanel.add(new JLabel("Bacheca:"));
        formPanel.add(boardCombo);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Salva");
        saveButton.addActionListener(e -> {
            try {
                saveNewTodo(dialog, titleField, descField, dueDateField, (String) boardCombo.getSelectedItem());
            } catch (SQLException ex) {
                showError("Errore nel salvataggio: " + ex.getMessage());
            }
        });

        buttonPanel.add(saveButton);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void saveNewTodo(JDialog dialog, JTextField titleField, JTextArea descField,
                             JTextField dueDateField, String boardName) throws SQLException {
        if (titleField.getText().trim().isEmpty()) {
            showError("Il titolo è obbligatorio");
            return;
        }

        Todo newTodo = new Todo();
        newTodo.setTitle(titleField.getText());
        newTodo.setDescription(descField.getText());
        newTodo.setDueDate(dueDateField.getText());
        newTodo.setBoard(boardName);

        todoController.addTodo(newTodo);
        refreshBoard(boardName);
        dialog.dispose();
    }

    private void refreshBoard(String boardName) {
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            if (tabbedPane.getTitleAt(i).equals(boardName)) {
                BoardPanel panel = (BoardPanel) tabbedPane.getComponentAt(i);
                panel.refreshTodos();
                break;
            }
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Errore", JOptionPane.ERROR_MESSAGE);
    }
}