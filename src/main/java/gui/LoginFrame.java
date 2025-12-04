package gui;

import javax.swing.*;
import java.awt.*;
import controller.ControllerGui;
// Nota: Non importiamo ControllerLogica perché la View parla solo con ControllerGui

public class LoginFrame extends JFrame {

    // Variabili per i componenti
    private JTextField userField;
    private JPasswordField passField;
    private JButton loginButton;

    // Riferimento al controller
    private ControllerGui controller;

    // IL COSTRUTTORE ORA ACCETTA ControllerGui
    public LoginFrame(ControllerGui controller) {
        this.controller = controller;

        // 1. Configurazione base della finestra
        setTitle("UniApp - Login");
        setSize(650, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la finestra nello schermo

        // Layout principale
        setLayout(new BorderLayout(0, 0));

        // --- PARTE CENTRALE (Input) ---
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 2, 5, 5)); // Aggiunto spaziatura 5px

        // Creiamo i componenti
        JLabel userLabel = new JLabel("Username:", JLabel.RIGHT); // Allineato a destra per estetica
        JLabel passLabel = new JLabel("Password:", JLabel.RIGHT);
        userField = new JTextField();
        passField = new JPasswordField();

        // Aggiunta alla griglia
        inputPanel.add(userLabel);
        inputPanel.add(userField);
        inputPanel.add(passLabel);
        inputPanel.add(passField);

        // Margini (ridotti leggermente per evitare che i campi spariscano se la finestra è piccola)
        inputPanel.setBorder(BorderFactory.createEmptyBorder(150, 100, 150, 100));

        add(inputPanel, BorderLayout.CENTER);

        // --- PARTE IN BASSO (Bottone) ---
        JPanel bottonPanel = new JPanel(); // Un pannello extra per non stretchare il bottone
        loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(100, 40)); // Dimensione fissa bottone
        bottonPanel.add(loginButton);

        add(bottonPanel, BorderLayout.SOUTH);

        // --- COLLEGAMENTO AZIONE ---
        // Impostiamo subito cosa succede quando clicchi
        loginButton.addActionListener(e -> {
            String username = getUsername();
            String password = getPassword();
            // Chiama il metodo del ControllerGui
            controller.handleLoginAttempt(username, password);
        });
    }

    /**
     * Ritorna l'Username inserito.
     */
    public String getUsername() {
        return userField.getText();
    }

    /**
     * Ritorna la Password inserita.
     */
    public String getPassword() {
        return new String(passField.getPassword());
    }

    /**
     * Mostra un messaggio di errore.
     */
    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message,
                "Errore di Login",
                JOptionPane.ERROR_MESSAGE);
    }
}