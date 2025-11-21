package gui;

import controller.ControllerLogica; // IMPORTANTE
import model.Bacheca;
import model.ToDo;
import model.Utente;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class HomeView extends JFrame {

    // ==========================================
    // 1. DICHIARAZIONE VARIABILI DI CLASSE
    // ==========================================
    private ControllerLogica controller;
    private Utente utenteCorrente;

    private JPanel mainPanel;
    private JButton btnUni;
    private JButton Lavorobutton; // Il bottone che volevi
    private JButton btnTempoLibero;
    private JButton btnLogout;

    // COSTRUTTORE AGGIORNATO
    public HomeView(ControllerLogica controller, Utente utenteCorrente) {
        super("Home Page - " + utenteCorrente.getLogin());

        // 2. ASSEGNAZIONE
        this.controller = controller;
        this.utenteCorrente = utenteCorrente;

        setupUI();
        setupListeners();

        this.setContentPane(mainPanel);
        this.setSize(800, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
    }

    private void setupUI() {
        mainPanel = new JPanel(new BorderLayout());

        // Header
        JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnLogout = new JButton("Logout");
        top.add(new JLabel("Utente: " + utenteCorrente.getLogin()));
        top.add(btnLogout);
        mainPanel.add(top, BorderLayout.NORTH);

        // Centro (Bacheche)
        JPanel center = new JPanel(new GridLayout(1, 3, 10, 10));
        btnUni = new JButton("Università");
        Lavorobutton = new JButton("Lavoro");
        btnTempoLibero = new JButton("Tempo Libero");

        // Colori per distinguerli
        btnUni.setBackground(new Color(173, 216, 230));
        Lavorobutton.setBackground(new Color(255, 228, 196));
        btnTempoLibero.setBackground(new Color(144, 238, 144));

        center.add(btnUni);
        center.add(Lavorobutton);
        center.add(btnTempoLibero);
        mainPanel.add(center, BorderLayout.CENTER);
    }

    private void setupListeners() {

        // LOGOUT
        btnLogout.addActionListener(e -> {
            new LoginView(controller).setVisible(true);
            this.dispose();
        });

        // 1. UNIVERSITÀ
        btnUni.addActionListener(e -> apriBacheca(1));

        // 2. LAVORO (Il tuo bottone specifico)
        Lavorobutton.addActionListener(e -> {
            // Qui controller e utenteCorrente sono visibili!
            apriBacheca(2);
        });

        // 3. TEMPO LIBERO
        btnTempoLibero.addActionListener(e -> apriBacheca(3));
    }

    // Metodo helper per aprire la vista bacheca
    private void apriBacheca(int id) {
        Bacheca b = controller.getBachecaCorrente(id);
        if (b != null) {
            // Passiamo ControllerLogica alla bacheca view
            BachecaView view = new BachecaView(controller, utenteCorrente, b);
            view.setVisible(true);
            this.dispose(); // Chiude la home
        } else {
            JOptionPane.showMessageDialog(this, "Errore caricamento bacheca");
        }
    }
}