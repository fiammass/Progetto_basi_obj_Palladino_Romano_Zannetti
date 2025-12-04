package gui;

import javax.swing.SwingUtilities;
import controller.ControllerGui;
import controller.ControllerLogica;
import model.Utente; // Importa Utente per definire la lista
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            // FIX: 0. Prepara il parametro richiesto dal ControllerLogica
            // Creiamo una lista vuota o predefinita per l'avvio
            List<Utente> listaUtentiIniziale = new ArrayList<>();
            // Assumi che ControllerLogica sia già istanziato e funzionante
            ControllerLogica logica = new ControllerLogica(listaUtentiIniziale);

            // 1. Crea il Controller GUI
            ControllerGui controller = new ControllerGui(logica);

            // 2. Crea la LoginFrame (View)
            LoginFrame loginView = new LoginFrame();

            // 3. COLLEGA LA VIEW AL CONTROLLER
            loginView.addListener(controller);

            // 4. (Opzionale, ma utile) Il Controller mantiene il riferimento alla View
            controller.setLoginFrame(loginView);

            // 5. Avvia
            loginView.setVisible(true);
        });
    }
}