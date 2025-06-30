package com.todomanager;

import com.todomanager.controller.AuthController;
import com.todomanager.database.DatabaseConnection;
import com.todomanager.gui.LoginWindow;
import javax.swing.*;

public class App {
    public static void main(String[] args) {
        try {
            // Configurazione iniziale
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            DatabaseConnection.initialize();

            // Inizializzazione controller
            AuthController authController = new AuthController();

            // Avvio GUI
            SwingUtilities.invokeLater(() -> {
                LoginWindow loginWindow = new LoginWindow(authController);
                loginWindow.setVisible(true);
            });

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Errore critico: " + e.getMessage(),
                    "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
}