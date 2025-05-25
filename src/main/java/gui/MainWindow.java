package gui;

import controller.TodoController;
import model.ToDo;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private final TodoController controller;
    private JList<ToDo> todoList;
    private DefaultListModel<ToDo> listModel;

    public MainWindow() {
        this.controller = new TodoController();
        setupUI();
    }

    private void setupUI() {
        setTitle("Gestore Todo");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Toolbar superiore
        JToolBar toolBar = new JToolBar();
        JButton addBtn = new JButton("Nuovo Todo");
        addBtn.addActionListener(e -> showTodoDialog());
        toolBar.add(addBtn);

        // Lista Todo
        listModel = new DefaultListModel<>();
        refreshTodoList();
        todoList = new JList<>(listModel);
        todoList.setCellRenderer(new TodoRenderer());
        todoList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Pannello dettagli
        JPanel detailPanel = new JPanel(new BorderLayout());
        detailPanel.add(new JLabel("Dettaglio Todo"), BorderLayout.NORTH);

        add(toolBar, BorderLayout.NORTH);
        add(new JScrollPane(todoList), BorderLayout.CENTER);
        add(detailPanel, BorderLayout.SOUTH);
    }

    private void refreshTodoList() {
        listModel.clear();
        controller.getTodosBachecaCorrente().forEach(listModel::addElement);
    }

    private void showTodoDialog() {
        new TodoDialog(this, controller).setVisible(true);
        refreshTodoList();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }
}