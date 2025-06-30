package gui.components;

import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel {
    public BoardPanel(String title) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);

        JPanel todosPanel = new JPanel();
        todosPanel.setLayout(new BoxLayout(todosPanel, BoxLayout.Y_AXIS));

        // Esempio Todo (da sostituire con dati reali)
        todosPanel.add(new TodoCard("Studiare Java", "2023-12-01"));
        todosPanel.add(new TodoCard("Fare esercizi", "2023-12-05"));

        JScrollPane scrollPane = new JScrollPane(todosPanel);
        add(scrollPane, BorderLayout.CENTER);
    }
}