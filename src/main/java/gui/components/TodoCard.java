package gui.components;

import javax.swing.*;
import java.awt.*;

public class TodoCard extends JPanel {
    public TodoCard(String title, String dueDate) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel titleLabel = new JLabel(title);
        JLabel dateLabel = new JLabel("Scade: " + dueDate);
        dateLabel.setForeground(Color.RED); // Sostituire con logica scadenza

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(new JButton("Modifica"));
        buttonPanel.add(new JButton("Elimina"));

        add(titleLabel, BorderLayout.NORTH);
        add(dateLabel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}