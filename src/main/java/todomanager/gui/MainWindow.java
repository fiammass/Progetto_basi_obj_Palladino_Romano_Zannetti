package com.todomanager.gui;

import com.todomanager.controller.TodoController;
import javax.swing.*;

public class MainWindow extends JFrame {
    private final TodoController todoController;

    public MainWindow() {
        this.todoController = new TodoController();
        setupUI();
    }

    private void setupUI() {
        setTitle("ToDo Manager");
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Università", new BoardPanel("Università", todoController));
        tabbedPane.addTab("Lavoro", new BoardPanel("Lavoro", todoController));
        tabbedPane.addTab("Tempo Libero", new BoardPanel("Tempo Libero", todoController));

        add(tabbedPane);
    }
}