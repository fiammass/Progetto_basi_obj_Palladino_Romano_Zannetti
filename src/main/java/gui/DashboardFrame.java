import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DashboardFrame extends JFrame {

    // Definizioni delle bacheche come variabili di classe per poterle leggere dai bottoni Cerca/Scadenze
    private BoardPanel pnlUniversita;
    private BoardPanel pnlLavoro;
    private BoardPanel pnlTempoLibero;

    public DashboardFrame() {
        setTitle("Gestore ToDo - Trello Clone");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra nello schermo

        setLayout(new BorderLayout());

        // 1. TOOLBAR (NORD)
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false); // Blocca la barra

        JButton btnCerca = new JButton("Cerca ToDo");
        JButton btnScad = new JButton("Scadenze Oggi");
        JButton btnLogout = new JButton("Logout");

        // Aggiungiamo icone o spazi
        toolbar.add(btnCerca);
        toolbar.addSeparator(); // Linea verticale
        toolbar.add(btnScad);

        // ("Glue" spinge il tasto logout tutto a destra)
        toolbar.add(Box.createHorizontalGlue());
        toolbar.add(btnLogout);

        add(toolbar, BorderLayout.NORTH);

        // 2. AREA CENTRALE (BACHECHE)
        // Usiamo GridLayout(1, 3) per avere 3 colonne fisse e con uno spazio equo
        JPanel mainGridPanel = new JPanel(new GridLayout(1, 3, 15, 0)); // 15px di spazio tra colonne
        mainGridPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // Margine esterno

        // Inizializziamo le 3 bacheche
        pnlUniversita = new BoardPanel("UNIVERSITÀ");
        pnlLavoro = new BoardPanel("LAVORO");
        pnlTempoLibero = new BoardPanel("TEMPO LIBERO");

        // DATI DI ESEMPIO (Per non vedere tutto vuoto all'avvio)
        // NOTA: Formato necessario gg/mm/aaaa per funzionare con "Scadenze Oggi"
        pnlUniversita.aggiungiCard("Esame Java", "28/11/2025", new Color(204, 255, 255)); // Azzurro
        pnlUniversita.aggiungiCard("Basi di Dati", "15/12/2025", new Color(255, 255, 153)); // Giallo

        pnlLavoro.aggiungiCard("Inviare Report", "28/11/2025", new Color(255, 204, 204)); // Rosso chiaro
        pnlLavoro.aggiungiCard("Riunione Team", "Domani", new Color(204, 255, 204)); // Verde

        pnlTempoLibero.aggiungiCard("Calcetto", "Venerdì", new Color(229, 204, 255)); // Viola

        // Aggiungiamo i pannelli alla griglia
        mainGridPanel.add(pnlUniversita);
        mainGridPanel.add(pnlLavoro);
        mainGridPanel.add(pnlTempoLibero);

        // Mettiamo tutto in uno ScrollPane (per ridimensionare la finestra)
        JScrollPane scrollPane = new JScrollPane(mainGridPanel);
        // Velocizza lo scroll del mouse
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);


        // 3. LOGICA DEI BOTTONI (EVENTI)
        // Lista di comodo per iterare su tutte le bacheche
        List<BoardPanel> tutteLeBacheche = Arrays.asList(pnlUniversita, pnlLavoro, pnlTempoLibero);

        // AZIONE: LOGOUT
        btnLogout.addActionListener(e -> {
            int conferma = JOptionPane.showConfirmDialog(this, "Vuoi davvero uscire?", "Logout", JOptionPane.YES_NO_OPTION);
            if (conferma == JOptionPane.YES_OPTION) {
                dispose(); // Chiude Dashboard
                new LoginFrame().setVisible(true); // Riapre Login
            }
        });

        // AZIONE: CERCA TODO
        btnCerca.addActionListener(e -> {
            String testo = JOptionPane.showInputDialog(this, "Inserisci il titolo da cercare:");

            if (testo != null && !testo.trim().isEmpty()) {
                List<ToDoCardPanel> risultati = new ArrayList<>();

                // Cerca in ogni bacheca
                for (BoardPanel bacheca : tutteLeBacheche) {
                    for (ToDoCardPanel card : bacheca.getCards()) {
                        // Controllo Case-Insensitive (ignora maiuscole/minuscole)
                        if (card.getTitolo().toLowerCase().contains(testo.toLowerCase())) {
                            risultati.add(card);
                        }
                    }
                }

                // Apre la finestra risultati
                new SearchResultsDialog(this, "Risultati ricerca: " + testo, risultati, "Varie").setVisible(true);
            }
        });

        // AZIONE: SCADENZE OGGI
        btnScad.addActionListener(e -> {
            // Calcola la data di oggi formattata (es. "28/11/2025")
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String oggi = LocalDate.now().format(dtf);

            List<ToDoCardPanel> inScadenza = new ArrayList<>();

            for (BoardPanel bacheca : tutteLeBacheche) {
                for (ToDoCardPanel card : bacheca.getCards()) {
                    // Confronta la stringa della data
                    if (card.getScadenza().trim().equals(oggi)) {
                        inScadenza.add(card);
                    }
                }
            }

            new SearchResultsDialog(this, "In scadenza Oggi (" + oggi + ")", inScadenza, "Varie").setVisible(true);
        });
    }

    // Main per testare singolarmente questa finestra
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DashboardFrame().setVisible(true));
    }
}