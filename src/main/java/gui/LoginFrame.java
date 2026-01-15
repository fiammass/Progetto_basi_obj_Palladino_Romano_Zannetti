package gui;

import javax.swing.*;
import java.awt.*;
import controller.ControllerGui;

/**
 * Finestra di Login dell'applicazione.
 * <p>
 * Questa classe estende {@link JFrame} e fornisce l'interfaccia grafica per l'autenticazione.
 * Contiene i campi per l'inserimento di username e password e un pulsante per inviare
 * la richiesta di accesso al {@link ControllerGui}.
 */
public class LoginFrame extends JFrame {

    private JTextField userField;
    private JPasswordField passField;
    private JButton loginButton;

    private ControllerGui controller;

    /**
     * Costruttore della classe LoginFrame.
     * Inizializza la finestra, imposta il layout, crea i componenti grafici (campi di testo, etichette, pulsanti)
     * e registra il listener per l'azione di login.
     *
     * @param controller Il riferimento al Controller GUI per gestire la logica di login.
     */
    public LoginFrame(ControllerGui controller) {
        this.controller = controller;

        setTitle("UniApp - Login");
        setSize(650, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(0, 0));

        // Pannello di Input (Centrale)
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 2, 5, 5));

        JLabel userLabel = new JLabel("Username:", JLabel.RIGHT);
        JLabel passLabel = new JLabel("Password:", JLabel.RIGHT);
        userField = new JTextField();
        passField = new JPasswordField();

        inputPanel.add(userLabel);
        inputPanel.add(userField);
        inputPanel.add(passLabel);
        inputPanel.add(passField);

        inputPanel.setBorder(BorderFactory.createEmptyBorder(150, 100, 150, 100));
        add(inputPanel, BorderLayout.CENTER);

        // Pannello del Bottone (Inferiore)
        JPanel bottonPanel = new JPanel();
        loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(100, 40));
        bottonPanel.add(loginButton);

        add(bottonPanel, BorderLayout.SOUTH);

        // Gestione Eventi
        loginButton.addActionListener(e -> {
            String username = getUsername();
            String password = getPassword();
            controller.handleLoginAttempt(username, password);
        });
    }

    /**
     * Restituisce lo username attualmente inserito nel campo di testo.
     *
     * @return La stringa contenente lo username.
     */
    public String getUsername() {
        return userField.getText();
    }

    /**
     * Restituisce la password attualmente inserita nel campo password.
     *
     * @return La stringa contenente la password.
     */
    public String getPassword() {
        return new String(passField.getPassword());
    }

    /**
     * Mostra una finestra di dialogo modale con un messaggio di errore.
     * Utile per notificare l'utente in caso di credenziali errate.
     *
     * @param message Il messaggio di errore da visualizzare.
     */
    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message,
                "Errore di Login",
                JOptionPane.ERROR_MESSAGE);
    }
}