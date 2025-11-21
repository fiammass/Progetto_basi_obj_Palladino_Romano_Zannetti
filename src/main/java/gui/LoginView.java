package gui;

import controller.ControllerLogica; // IMPORTANTE: Usa la tua classe rinominata
import model.Utente;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginView extends JFrame {

    // VARIABILE DI CLASSE (Scope globale)
    private ControllerLogica controller;

    private JPanel mainPanel;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnRegister;

    // COSTRUTTORE AGGIORNATO
    public LoginView(ControllerLogica controller) {
        super("ToDo App - Login");

        // ASSEGNAZIONE
        this.controller = controller;

        setupUI();
        setupListeners();

        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 350);
        this.setLocationRelativeTo(null);
    }

    private void setupUI() {
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("Username:"), gbc);
        gbc.gridy = 1;
        txtUsername = new JTextField(15);
        mainPanel.add(txtUsername, gbc);

        // Password
        gbc.gridy = 2;
        mainPanel.add(new JLabel("Password:"), gbc);
        gbc.gridy = 3;
        txtPassword = new JPasswordField(15);
        mainPanel.add(txtPassword, gbc);

        // Bottoni
        JPanel btnPanel = new JPanel();
        btnLogin = new JButton("Login");
        btnRegister = new JButton("Registrati");
        btnPanel.add(btnLogin);
        btnPanel.add(btnRegister);

        gbc.gridy = 4;
        mainPanel.add(btnPanel, gbc);
    }

    private void setupListeners() {
        btnLogin.addActionListener(e -> {
            String user = txtUsername.getText();
            String pass = new String(txtPassword.getPassword());

            // CHIAMATA AL CONTROLLER
            if (controller.checkLogin(user, pass)) {
                // Recupera l'utente popolato
                Utente u = controller.getUtenteCorrente();

                // Passa ControllerLogica e Utente alla Home
                HomeView home = new HomeView(controller, u);
                home.setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Credenziali errate!");
            }
        });

        btnRegister.addActionListener(e -> {
            String user = txtUsername.getText();
            String pass = new String(txtPassword.getPassword());
            if(!user.isEmpty() && !pass.isEmpty()) {
                controller.aggiungiUtente(user, pass);
                JOptionPane.showMessageDialog(this, "Registrato! Ora fai il login.");
            }
        });
    }
}