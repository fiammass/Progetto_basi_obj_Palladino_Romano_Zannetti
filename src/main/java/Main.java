import controller.ControllerLogica;
import controller.ControllerGui;
import gui.LoginFrame;
import javax.swing.SwingUtilities;

/**
 * Classe principale dell'applicazione (Entry Point).
 * <p>
 * Questa classe contiene il metodo {@code main} che si occupa di avviare il thread grafico (EDT),
 * inizializzare i controller (Logica e GUI) secondo il pattern MVC e mostrare la prima finestra di login.
 */
public class Main {

    /**
     * Punto di ingresso del programma.
     * Utilizza {@link SwingUtilities#invokeLater(Runnable)} per garantire che l'interfaccia grafica
     * venga creata e gestita all'interno dell'Event Dispatch Thread (EDT), come richiesto dalle specifiche Swing.
     *
     * @param args Argomenti da riga di comando (non utilizzati in questa applicazione).
     */
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            ControllerLogica controllerLogica = new ControllerLogica();
            ControllerGui controllerGui = new ControllerGui(controllerLogica);
            LoginFrame loginView = new LoginFrame(controllerGui);

            controllerGui.setLoginFrame(loginView);
            loginView.setVisible(true);
        });
    }
}