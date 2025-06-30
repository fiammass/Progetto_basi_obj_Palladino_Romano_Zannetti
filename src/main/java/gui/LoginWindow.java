package gui;

import javax.swing.*;
import java.awt.*;
import controller.AuthController;

public class LoginWindow extends JFrame {
    private final AuthController authController;

    public LoginWindow(AuthController authController) {
        this.authController = authController;
        setTitle("Login");
        setSize(350, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> attemptLogin(usernameField, passwordField));

        panel.add(new JLabel(""));
        panel.add(loginButton);

        add(panel);
    }

    private void attemptLogin(JTextField usernameField, JPasswordField passwordField) {
        try {
            if (authController.login(
                    usernameField.getText(),
                    new String(passwordField.getPassword())
            )) {
                new MainWindow().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Credenziali errate");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Errore di connessione");
        }
    }
}