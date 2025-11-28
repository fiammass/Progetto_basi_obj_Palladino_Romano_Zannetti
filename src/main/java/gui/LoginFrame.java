import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class LoginFrame extends JFrame {

    // Variabili per i campi di testo
    private JTextField userField;
    private JPasswordField passField;

    public <getCenterPoint> LoginFrame() {
        // 1. Configurazione base della finestra
        setTitle("UniApp - Login");
        setSize(650, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la finestra nello schermo
        getCenterPoint GraphicsEnvironment;

        // Impostiamo il layout principale a BorderLayout
        setLayout(new BorderLayout(0, 0));

        // PARTE CENTRALE (Input)
        JPanel inputPanel = new JPanel();

        // QUI c'è la griglia: 2 righe, 2 colonne, spaziatura 5px
        inputPanel.setLayout(new GridLayout(2, 2, 0, 0));

        // Creiamo i componenti
        JLabel userLabel = new JLabel("Username:", JLabel.LEFT);
        JLabel passLabel = new JLabel("Password:", JLabel.LEFT);
        userField = new JTextField();       // Inizializzazione campo testo
        passField = new JPasswordField();   // Inizializzazione campo password

        // Li aggiungiamo alla griglia nell'ordine di lettura (Sinistra->Destra, Alto->Basso)
        inputPanel.add(userLabel);  // Riga 1, Col 1
        inputPanel.add(userField);  // Riga 1, Col 2
        inputPanel.add(passLabel);  // Riga 2, Col 1
        inputPanel.add(passField);  // Riga 2, Col 2

        // Bordo vuoto per estetica
        inputPanel.setBorder(BorderFactory.createEmptyBorder(160, 100, 160, 100));

        // Aggiungiamo il pannello al CENTRO
        add(inputPanel, BorderLayout.CENTER);


        // PARTE IN BASSO (Bottone)
        JButton loginButton = new JButton("Login");

        loginButton.addActionListener(e -> {
            System.out.println("Login effettuato! Passo alla Dashboard.");

            // COLLEGAMENTO 2: Chiudi Login e apri Dashboard
            this.dispose(); // "Uccide" la finestra di Login
            new DashboardFrame().setVisible(true); // Crea e mostra la Dashboard
        });

        // Bottone messo a SUD
        add(loginButton, BorderLayout.SOUTH);
    }

    // Metodo main per avviare
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}