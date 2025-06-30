import controller.AuthController;
import controller.TodoController;
import database.DatabaseConnection;
import database.DatabaseSchema;
import dao.UserDAO;
import dao.TodoDAO;
import postgresdao.PostgresUserDAO;
import postgresdao.PostgresTodoDAO;
import gui.LoginWindow;
import javax.swing.*;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) {
        try {
            // 1. Configurazione iniziale
            setSystemLookAndFeel();

            // 2. Setup database
            DatabaseConnection.init("jdbc:postgresql://localhost:5432/todo_db", "postgres", "password");
            DatabaseSchema.createTables();

            // 3. Inizializzazione componenti
            UserDAO userDao = new PostgresUserDAO();
            TodoDAO todoDao = new PostgresTodoDAO();

            AuthController authController = new AuthController(userDao);
            TodoController todoController = new TodoController(todoDao);

            // 4. Avvio GUI
            SwingUtilities.invokeLater(() -> {
                LoginWindow loginWindow = new LoginWindow(authController, todoController);
                loginWindow.setVisible(true);
            });

        } catch (SQLException e) {
            showFatalError("Database Error", e.getMessage());
        } catch (Exception e) {
            showFatalError("Application Error", e.getMessage());
        }
    }

    private static void setSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Warning: Couldn't set system look and feel");
        }
    }

    private static void showFatalError(String title, String message) {
        JOptionPane.showMessageDialog(
                null,
                message,
                title,
                JOptionPane.ERROR_MESSAGE
        );
        System.exit(1);
    }
}