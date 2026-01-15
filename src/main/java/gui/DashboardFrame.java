package gui;

import controller.ControllerGui;
import model.Bacheca;
import model.ToDo;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * Finestra principale (Dashboard) dell'applicazione.
 * <p>
 * Questa classe rappresenta l'interfaccia principale visibile dopo il login.
 * Contiene la barra degli strumenti (Toolbar) e le tre colonne ({@link BoardPanel})
 * corrispondenti alle bacheche Università, Lavoro e Tempo Libero.
 * Funge da contenitore visivo e gestisce l'aggiornamento della vista in base ai dati del modello.
 */
public class DashboardFrame extends JFrame {

    private BoardPanel pnlUniversita;
    private BoardPanel pnlLavoro;
    private BoardPanel pnlTempoLibero;

    private JButton btnCerca;
    private JButton btnScad;
    private JButton btnLogout;

    private List<Bacheca> bachequesModel;
    private ControllerGui controller;

    /**
     * Costruttore della classe DashboardFrame.
     * Inizializza le proprietà della finestra, il layout, la toolbar superiore
     * e la griglia contenente i pannelli delle tre bacheche.
     */
    public DashboardFrame() {
        setTitle("Gestore ToDo - Trello Clone");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // TOOLBAR
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        btnCerca = new JButton("Cerca ToDo");
        btnScad = new JButton("Scadenze Oggi");
        btnLogout = new JButton("Logout");

        toolbar.add(btnCerca);
        toolbar.addSeparator();
        toolbar.add(btnScad);
        toolbar.add(Box.createHorizontalGlue());
        toolbar.add(btnLogout);
        add(toolbar, BorderLayout.NORTH);

        // GRIGLIA BACHECHE
        JPanel mainGridPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        mainGridPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        pnlUniversita = new BoardPanel("UNIVERSITÀ");
        pnlLavoro = new BoardPanel("LAVORO");
        pnlTempoLibero = new BoardPanel("TEMPO LIBERO");

        mainGridPanel.add(pnlUniversita);
        mainGridPanel.add(pnlLavoro);
        mainGridPanel.add(pnlTempoLibero);

        add(mainGridPanel, BorderLayout.CENTER);
    }

    /**
     * Collega il ControllerGui agli elementi interattivi della Dashboard.
     * Registra i listener per i pulsanti della toolbar e propaga il controller
     * ai pannelli delle bacheche affinché possano gestire i propri eventi.
     *
     * @param controller L'istanza del controller GUI.
     */
    public void addListener(ControllerGui controller) {
        this.controller = controller;

        pnlUniversita.addListener(controller);
        pnlLavoro.addListener(controller);
        pnlTempoLibero.addListener(controller);

        btnLogout.addActionListener(e -> controller.handleLogout());

        btnCerca.addActionListener(e -> {
            String testo = JOptionPane.showInputDialog(this, "Inserisci titolo:");
            if (testo != null && !testo.trim().isEmpty()) controller.handleCercaToDo(testo);
        });

        btnScad.addActionListener(e -> controller.handleScadenzeOggi());
    }

    /**
     * Popola l'interfaccia grafica con i dati delle bacheche recuperati dal database.
     * Pulisce il contenuto attuale dei pannelli e ricrea le card per ogni ToDo.
     *
     * @param b1 Il modello della prima bacheca (Università).
     * @param b2 Il modello della seconda bacheca (Lavoro).
     * @param b3 Il modello della terza bacheca (Tempo Libero).
     */
    public void displayBacheche(Bacheca b1, Bacheca b2, Bacheca b3) {
        this.bachequesModel = Arrays.asList(b1, b2, b3);

        pnlUniversita.setTitoloBacheca(b1.getTitolo());
        pnlUniversita.svuotaCard();

        pnlLavoro.setTitoloBacheca(b2.getTitolo());
        pnlLavoro.svuotaCard();

        pnlTempoLibero.setTitoloBacheca(b3.getTitolo());
        pnlTempoLibero.svuotaCard();

        if (b1.getTodos() != null) for (ToDo t : b1.getTodos()) pnlUniversita.aggiungiCard(t);
        if (b2.getTodos() != null) for (ToDo t : b2.getTodos()) pnlLavoro.aggiungiCard(t);
        if (b3.getTodos() != null) for (ToDo t : b3.getTodos()) pnlTempoLibero.aggiungiCard(t);
    }

    /**
     * Aggiunge visivamente un nuovo ToDo al pannello corretto senza ricaricare tutta la dashboard.
     *
     * @param bachecaId L'ID della bacheca di destinazione.
     * @param nuovoToDo L'oggetto ToDo appena creato.
     */
    public void aggiornaPanel(int bachecaId, ToDo nuovoToDo) {
        BoardPanel target = getPanelByModelId(bachecaId);
        if (target != null) {
            target.aggiungiCard(nuovoToDo);
        }
    }

    /**
     * Recupera il modello dati (Bacheca) associato a uno specifico pannello grafico.
     * Utile per capire in quale bacheca l'utente sta cercando di aggiungere un ToDo.
     *
     * @param panel Il pannello grafico (BoardPanel).
     * @return L'oggetto Bacheca corrispondente, o null se non trovato.
     */
    public Bacheca getBachecaModelForPanel(BoardPanel panel) {
        if (bachequesModel == null) return null;
        if (panel == pnlUniversita) return bachequesModel.get(0);
        if (panel == pnlLavoro) return bachequesModel.get(1);
        if (panel == pnlTempoLibero) return bachequesModel.get(2);
        return null;
    }

    /**
     * Metodo helper privato per trovare il pannello grafico corrispondente a un ID di bacheca.
     *
     * @param id L'ID della bacheca nel database.
     * @return Il pannello grafico corrispondente.
     */
    private BoardPanel getPanelByModelId(int id) {
        if (bachequesModel == null) return null;
        for (Bacheca b : bachequesModel) {
            if (b.getIdBa() == id) {
                if (b == bachequesModel.get(0)) return pnlUniversita;
                if (b == bachequesModel.get(1)) return pnlLavoro;
                if (b == bachequesModel.get(2)) return pnlTempoLibero;
            }
        }
        return null;
    }
}