package gui;

import controller.ControllerGui;
import model.ToDo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ToDoCardPanel extends JPanel {

    private JLabel lblTitolo;
    private JLabel lblScadenza;

    private ToDo todo;
    private ControllerGui controller;

    // Colori
    private static final Color COLORE_SCADUTO = new Color(255, 102, 102); // Rosso
    private static final Color COLORE_COMPLETATO = new Color(144, 238, 144); // Verde
    private static final Color COLORE_DEFAULT = new Color(255, 255, 204); // Giallo Post-it

    public ToDoCardPanel(ControllerGui controller, ToDo todo) {
        this.controller = controller;
        this.todo = todo;

        initUI();
        initListeners();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // FONDAMENTALE: Rende il pannello opaco, altrimenti lo sfondo non cambia!
        setOpaque(true);

        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        lblTitolo = new JLabel();
        lblTitolo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitolo.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));

        lblScadenza = new JLabel();
        lblScadenza.setFont(new Font("Arial", Font.ITALIC, 11));
        lblScadenza.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));

        add(lblTitolo, BorderLayout.CENTER);
        add(lblScadenza, BorderLayout.SOUTH);

        // Prima colorazione
        aggiornaGrafica();

        setPreferredSize(new Dimension(250, 60));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void initListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (controller != null) {
                    controller.handleModificaToDo(ToDoCardPanel.this);
                }
            }
        });
    }

    /**
     * Aggiorna il colore e i testi in base allo stato attuale del ToDo
     */
    public void aggiornaGrafica() {
        boolean completato = Boolean.TRUE.equals(todo.getCompletato());
        boolean scaduto = false;

        // Verifica Scadenza (Solo se ha data e non è completato)
        if (todo.getDatescadenza() != null &&
                todo.getDatescadenza().isBefore(LocalDate.now()) &&
                !completato) {
            scaduto = true;
        }

        // --- LOGICA COLORI ---
        if (completato) {
            setBackground(COLORE_COMPLETATO); // Verde vince su tutto
        } else if (scaduto) {
            setBackground(COLORE_SCADUTO);    // Rosso vince su colore utente
        } else {
            // Se normale, usa il colore dell'oggetto ToDo (o default)
            if (todo.getColor() != null) {
                setBackground(todo.getColor());
            } else {
                setBackground(COLORE_DEFAULT);
            }
        }

        // --- LOGICA TESTI ---
        if (completato) {
            lblTitolo.setText("<html><strike>" + todo.getTitolo() + "</strike></html>");
            lblTitolo.setForeground(new Color(0, 100, 0));
        } else {
            lblTitolo.setText(todo.getTitolo());
            lblTitolo.setForeground(Color.BLACK);
        }

        // --- LOGICA DATA ---
        if (todo.getDatescadenza() != null) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String dataText = "Scad: " + todo.getDatescadenza().format(dtf);

            if (scaduto) {
                lblScadenza.setText(dataText + " (SCADUTO!)");
                lblScadenza.setForeground(Color.WHITE);
            } else {
                lblScadenza.setText(dataText);
                lblScadenza.setForeground(Color.BLACK);
            }
        } else {
            lblScadenza.setText("Scad: --/--/----");
            lblScadenza.setForeground(Color.BLACK);
        }

        // Forza il ridisegno immediato
        revalidate();
        repaint();
    }

    public ToDo getToDo() {
        return todo;
    }
}