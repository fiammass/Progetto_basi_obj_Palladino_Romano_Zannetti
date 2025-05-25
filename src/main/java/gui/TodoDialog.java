package gui;

import controller.TodoController;
import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class TodoDialog extends JDialog {
    private final TodoController controller;
    private JTextField titleField;
    private JSpinner dateSpinner;

    public TodoDialog(JFrame parent, TodoController controller) {
        super(parent, "Aggiungi Todo", true);
        this.controller = controller;
        setupUI();
    }

    private void setupUI() {
        setSize(400, 200);
        setLayout(new GridLayout(3, 2));

        add(new JLabel("Titolo:"));
        titleField = new JTextField();
        add(titleField);

        add(new JLabel("Scadenza:"));
        dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");
        dateSpinner.setEditor(editor);
        add(dateSpinner);

        JButton saveBtn = new JButton("Salva");
        saveBtn.addActionListener(e -> saveTodo());
        add(saveBtn);
    }

    private void saveTodo() {
        String title = titleField.getText();
        Date date = (Date) dateSpinner.getValue();

        if (!title.isEmpty() && date != null) {
            controller.aggiungiTodo(title, date);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Compila tutti i campi!");
        }
    }
}