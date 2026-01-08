import controller.ControllerLogica;
import controller.ControllerGui;
import gui.LoginFrame;
import javax.swing.SwingUtilities;

/**
 * Classe principlae di esecuzione del programma
 */
public class Main {

    /**
     * Punto di inzio dell' applicazione
     * @param args
     */

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            ControllerLogica controllerLogica = new ControllerLogica();

            // 2. Inizializza il Controller GUI (Il "ponte" tra grafica e logica)
            ControllerGui controllerGui = new ControllerGui(controllerLogica);

            // 3. Avvia la prima finestra (Login) passandole il controller GUI
            LoginFrame loginView = new LoginFrame(controllerGui);

            // Collega la view al controller
            controllerGui.setLoginFrame(loginView);

            loginView.setVisible(true);
        });
    }
}