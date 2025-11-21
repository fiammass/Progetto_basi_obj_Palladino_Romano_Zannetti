package gui;

import controller.ControllerLogica; // IMPORTANTE
import model.Bacheca;
import model.ToDo;
import model.Utente;

import javax.swing.*;
import java.awt.*;

public class BachecaView extends JFrame {

    // VARIABILI DI CLASSE
    private ControllerLogica controller;
    private Utente utenteCorrente;
    private Bacheca bachecaCorrente;

    private DefaultListModel<ToDo> listModel;
    private JList<ToDo> todoList;
    private JButton btnIndietro;

    // COSTRUTTORE AGGIORNATO
    public BachecaView(ControllerLogica controller, Utente utenteCorrente, Bacheca bachecaCorrente) {
        super("Bacheca: " + bachecaCorrente.getTitolo());

        // ASSEGNAZIONE
        this.controller = controller;
        this.utenteCorrente = utenteCorrente;
        this.bachecaCorrente = bachecaCorrente;

        setupUI();
        popolaLista();

        // Listener semplice per tornare indietro
        btnIndietro.addActionListener(e -> {
            new HomeView(controller, utenteCorrente).setVisible(true);
            this.dispose();
        });

        this.setSize(600, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
    }

    private void setupUI() {
        JPanel panel = new JPanel(new BorderLayout());

        // Titolo in alto
        JLabel title = new JLabel(bachecaCorrente.getTitolo(), SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(title, BorderLayout.NORTH);

        // Lista centrale
        listModel = new DefaultListModel<>();
        todoList = new JList<>(listModel);
        panel.add(new JScrollPane(todoList), BorderLayout.CENTER);

        // Bottone in basso
        btnIndietro = new JButton("Torna alla Home");
        panel.add(btnIndietro, BorderLayout.SOUTH);

        this.setContentPane(panel);
    }

    private void popolaLista() {
        listModel.clear();
        if (bachecaCorrente.getTodos() != null) {
            for (ToDo t : bachecaCorrente.getTodos()) {
                listModel.addElement(t);
            }
        }
    }
}