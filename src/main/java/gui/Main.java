import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // 1. Crea la finestra di Login
                LoginFrame finestraLogin = new LoginFrame();

                // 2. Rendila visibile sullo schermo
                finestraLogin.setVisible(true);
            }
        });
    }
}
