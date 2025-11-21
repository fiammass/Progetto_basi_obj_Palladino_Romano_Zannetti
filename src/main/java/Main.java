import controller.ControllerLogica;
import gui.LoginView;
import model.Utente;
import java.util.ArrayList;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Avvia l'interfaccia nel thread grafico corretto
        SwingUtilities.invokeLater(() -> {
            // 1. Crea la lista utenti vuota (il DB farà il resto)
            java.util.List<Utente> utenti = new ArrayList<>();

            // 2. Inizializza il TUO ControllerLogica
            ControllerLogica controller = new ControllerLogica(utenti);

            // 3. Avvia la LoginView passandole il controller
            LoginView loginView = new LoginView(controller);
            loginView.setVisible(true);
        });
    }
}