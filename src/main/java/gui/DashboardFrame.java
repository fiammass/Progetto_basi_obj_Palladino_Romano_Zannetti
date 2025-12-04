package gui;

import controller.ControllerGui;
import javax.swing.*;
import model.Bacheca;
import model.ToDo;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;


public class DashboardFrame extends JFrame {

    // Definizioni delle bacheche come variabili di classe per poterle leggere dai bottoni Cerca/Scadenze
    private BoardPanel pnlUniversita;
    private BoardPanel pnlLavoro;
    private BoardPanel pnlTempoLibero;

    // Variabili per i bottoni che devono essere gestiti dal Controller
    private JButton btnCerca;
    private JButton btnScad;
    private JButton btnLogout;

    private List<Bacheca> bachequesModel; // Memorizza i riferimenti al Model

    public DashboardFrame() {
        setTitle("Gestore ToDo - Trello Clone");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra nello schermo

        setLayout(new BorderLayout());

        // 1. TOOLBAR (NORD)
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false); // Blocca la barra

        btnCerca = new JButton("Cerca ToDo");
        btnScad = new JButton("Scadenze Oggi");
        btnLogout = new JButton("Logout");

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

        // Aggiungiamo i pannelli alla griglia
        mainGridPanel.add(pnlUniversita);
        mainGridPanel.add(pnlLavoro);
        mainGridPanel.add(pnlTempoLibero);

        // Mettiamo tutto in uno ScrollPane (per ridimensionare la finestra)
        JScrollPane scrollPane = new JScrollPane(mainGridPanel);
        // Velocizza lo scroll del mouse
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
    }


       public void addListener(ControllerGui controller) {
           // Collega i listener dei BoardPanel al Controller
           pnlUniversita.addListener(controller);
           pnlLavoro.addListener(controller);
           pnlTempoLibero.addListener(controller);
           // Azione Logout (delegata)
           btnLogout.addActionListener(e -> {
               int conferma = JOptionPane.showConfirmDialog(this, "Vuoi davvero uscire?", "Logout", JOptionPane.YES_NO_OPTION);
               if (conferma == JOptionPane.YES_OPTION) {
                   // La view chiede al Controller di gestire l'uscita
                   controller.handleLogout();
               }
           });
           // Azione cerca Todo (delega)
           btnCerca.addActionListener(e -> {
               String testo = JOptionPane.showInputDialog(this, "Inserisci il titolo da cercare:");
               if (testo != null && !testo.trim().isEmpty()) {
                   // La View raccoglie l'input e lo passa al Controller
                   controller.handleCercaToDo(testo);
               }

           });

           // AZIONE: SCADENZE OGGI
           btnScad.addActionListener(e -> {
               // La view chiede al Controller di eseguire la logica delle scadenze
               controller.handleScadenzeOggi();
           });
       }
            // Metodo per il Controller: Popola la View con i dati del Model
            public void displayBacheche(Bacheca b1, Bacheca b2, Bacheca b3) {
                this.bachequesModel = Arrays.asList(b1, b2, b3);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                // Aggiorna i titoli dei pannelli (Logica di Visualizzazione)
                pnlUniversita.setTitoloBacheca(b1.getTitolo());
                pnlLavoro.setTitoloBacheca(b2.getTitolo());
                pnlTempoLibero.setTitoloBacheca(b3.getTitolo());

                pnlUniversita.svuotaCard();
                pnlLavoro.svuotaCard();
                pnlTempoLibero.svuotaCard();

                // Popola i pannelli con i ToDo (Logica di Visualizzazione)
                for (ToDo todo : b1.getTodos()) {
                    // FIX: Conversione di LocalDate in String per BoardPanel.aggiungiCard
                    String dataStr = todo.getDatescadenza() != null ? todo.getDatescadenza().format(dtf) : "N/D";
                    pnlUniversita.aggiungiCard(todo.getTitolo(), dataStr, todo.getColor());
                }
                for (ToDo todo : b2.getTodos()) {
                    String dataStr = todo.getDatescadenza() != null ? todo.getDatescadenza().format(dtf) : "N/D";
                    pnlLavoro.aggiungiCard(todo.getTitolo(), dataStr, todo.getColor());
                }
                for (ToDo todo : b3.getTodos()) {
                    String dataStr = todo.getDatescadenza() != null ? todo.getDatescadenza().format(dtf) : "N/D";
                    pnlTempoLibero.aggiungiCard(todo.getTitolo(), dataStr, todo.getColor());
                }
            }
// Metodo per il Controller: Aggiorna un pannello specifico
            public void aggiornaPanel(int bachecaId, String titolo, LocalDate scadenza, Color colore) {
                BoardPanel targetPanel = getPanelByModelId(bachecaId);
                if (targetPanel != null) {
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    String dataStr = scadenza != null ? scadenza.format(dtf) : "N/D";
                    targetPanel.aggiungiCard(titolo, dataStr, colore);
                }
            }

            // Metodo helper per mappare la View al Model (necessario in ControllerGui)
            public Bacheca getBachecaModelForPanel(BoardPanel panel) {
                if (bachequesModel == null) return null;
                if (panel == pnlUniversita) return bachequesModel.get(0);
                if (panel == pnlLavoro) return bachequesModel.get(1);
                if (panel == pnlTempoLibero) return bachequesModel.get(2);
                return null;
            }

            // Metodo helper per mappare l'ID del Model al Panel View
            private BoardPanel getPanelByModelId(int id) {
                if (bachequesModel == null || bachequesModel.size() < 3) return null;
                if (bachequesModel.get(0).getIdBa() == id) return pnlUniversita;
                if (bachequesModel.get(1).getIdBa() == id) return pnlLavoro;
                if (bachequesModel.get(2).getIdBa() == id) return pnlTempoLibero;
                return null;
            }
        }