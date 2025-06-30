import database.DatabaseSchema;
import view.LoginView;
import controller.AuthController;

public class App {
    public static void main(String[] args) {
        try {
            // 1. Inizializza DB
            DatabaseSchema.createTables();

            // 2. Crea i DAO
            UserDAO userDao = new PostgresUserDAO();

            // 3. Passa ai Controller
            AuthController authController = new AuthController(userDao);

            // 4. Avvia GUI
            SwingUtilities.invokeLater(() -> {
                new LoginView(authController).setVisible(true);
            });

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Errore database: " + e.getMessage());
            System.exit(1);
        }
    }
}