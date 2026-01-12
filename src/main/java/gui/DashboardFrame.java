package gui;

import controller.ControllerGui;
import model.Bacheca;
import model.ToDo;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class DashboardFrame extends JFrame {

    private BoardPanel pnlUniversita;
    private BoardPanel pnlLavoro;
    private BoardPanel pnlTempoLibero;

    private JButton btnCerca;
    private JButton btnScad;
    private JButton btnLogout;

    private List<Bacheca> bachequesModel;
    private ControllerGui controller; // Campo per salvare il controller

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

    public void addListener(ControllerGui controller) {
        this.controller = controller; // IMPORTANTE: Salva il riferimento

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
     * Carica tutte le bacheche passando i dati reali
     */
    public void displayBacheche(Bacheca b1, Bacheca b2, Bacheca b3) {
        this.bachequesModel = Arrays.asList(b1, b2, b3);

        // Configura titoli e svuota
        pnlUniversita.setTitoloBacheca(b1.getTitolo());
        pnlUniversita.svuotaCard();

        pnlLavoro.setTitoloBacheca(b2.getTitolo());
        pnlLavoro.svuotaCard();

        pnlTempoLibero.setTitoloBacheca(b3.getTitolo());
        pnlTempoLibero.svuotaCard();

        // Popola usando gli oggetti ToDo
        if (b1.getTodos() != null) for (ToDo t : b1.getTodos()) pnlUniversita.aggiungiCard(t);
        if (b2.getTodos() != null) for (ToDo t : b2.getTodos()) pnlLavoro.aggiungiCard(t);
        if (b3.getTodos() != null) for (ToDo t : b3.getTodos()) pnlTempoLibero.aggiungiCard(t);
    }

    /**
     * Aggiorna un singolo pannello con un nuovo ToDo
     */
    public void aggiornaPanel(int bachecaId, ToDo nuovoToDo) {
        BoardPanel target = getPanelByModelId(bachecaId);
        if (target != null) {
            target.aggiungiCard(nuovoToDo);
        }
    }

    public Bacheca getBachecaModelForPanel(BoardPanel panel) {
        if (bachequesModel == null) return null;
        if (panel == pnlUniversita) return bachequesModel.get(0);
        if (panel == pnlLavoro) return bachequesModel.get(1);
        if (panel == pnlTempoLibero) return bachequesModel.get(2);
        return null;
    }

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