package gui;

import controller.TodoController;
import gui.components.BoardPanel;
import model.Board;
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
        loadBoards();
    }

    private void initializeUI() {
        setTitle("ToDo Manager");
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout principale
        setLayout(new BorderLayout());

        // Creazione toolbar
        JToolBar toolBar = new JToolBar();
        JButton addButton = new JButton("+ Nuovo Todo");
        addButton.addActionListener(e -> showAddTodoDialog());
        toolBar.add(addButton);

        // Area principale a schede
        tabbedPane = new JTabbedPane();

        add(toolBar, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }

    private void loadBoards() {
        try {
            // Carica le bacheche predefinite
            addBoard("Università");
            addBoard("Lavoro");
            addBoard("Tempo Libero");

            // Carica bacheche personalizzate se presenti
            List<Board> customBoards = todoController.getAllBoards();
            for (Board board : customBoards) {
                addBoard(board.getName());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Errore nel caricamento delle bacheche: " + e.getMessage(),
                    "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addBoard(String boardName) {
        try {
            BoardPanel panel = new BoardPanel(boardName);
            List<Todo> todos = todoController.getTodosByBoard(boardName);

            for (Todo todo : todos) {
                panel.addTodoCard(todo);
            }

            tabbedPane.addTab(boardName, panel);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Errore nel caricamento dei todo: " + e.getMessage(),
                    "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showAddTodoDialog() {
        JDialog dialog = new JDialog(this, "Nuovo Todo", true);
        dialog.setSize(400, 300);

        // Implementa qui il form per aggiungere un nuovo todo
        // Esempio:
        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Titolo:"));
        JTextField titleField = new JTextField();
        panel.add(titleField);

        JButton saveButton = new JButton("Salva");
        saveButton.addActionListener(e -> {
            try {
                Todo newTodo = new Todo();
                newTodo.setTitle(titleField.getText());
                newTodo.setBoard(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()));

                todoController.addTodo(newTodo);
                refreshCurrentBoard();
                dialog.dispose();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Errore nel salvataggio: " + ex.getMessage(),
                        "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(saveButton);
        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void refreshCurrentBoard() {
        int selectedIndex = tabbedPane.getSelectedIndex();
        String boardName = tabbedPane.getTitleAt(selectedIndex);
        tabbedPane.remove(selectedIndex);
        addBoard(boardName);
        tabbedPane.setSelectedIndex(selectedIndex);
    }

    public static void main(String[] args) {
        // Esempio di utilizzo (da sostituire con la tua inizializzazione)
        SwingUtilities.invokeLater(() -> {
            try {
                TodoController controller = new TodoController(); // Inizializza con i tuoi DAO
                new MainWindow(controller).setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Errore nell'avvio dell'applicazione: " + e.getMessage(),
                        "Errore Critico", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}