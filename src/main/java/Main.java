import controller.ControllerLogica;
import controller.ControllerGui;
import gui.LoginFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 1. Inizializza la Logica (Il Model/DB)
            // Non passiamo più liste vuote, il controller si arrangia con i DAO.
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