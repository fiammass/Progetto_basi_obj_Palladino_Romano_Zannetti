package gui;

import controller.ControllerGui;
import model.Bacheca;
import model.CheckList;
import model.ToDo;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Finestra di dialogo (JDialog) modale per la creazione o la modifica di un ToDo.
 * <p>
 * Questa classe gestisce:
 * <ul>
 * <li>L'inserimento dei dati principali (Titolo, Descrizione, Scadenza, Colore).</li>
 * <li>La gestione della Checklist associata (Aggiunta, Rimozione, Spunta attività).</li>
 * <li>L'aggiornamento visivo immediato dello stato "Completato" se la checklist viene finita.</li>
 * </ul>
 */
public class ToDoEditorDialog extends JDialog {

    // --- Componenti UI Dati Base ---
    private JTextField txtTitolo;
    private JTextArea txtDescrizione;
    private JTextField txtData;
    private JCheckBox chkCompletato; // Checkbox principale dello stato del ToDo
    private JButton btnColore;
    private JButton btnSalva;
    private JButton btnElimina;

    // --- Componenti UI Checklist ---
    private JPanel pnlCheckListContainer; // Pannello scrollabile che contiene le righe della checklist
    private JTextField txtNuovaAttivita;  // Campo input per nuove voci
    private JButton btnAddAttivita;       // Bottone '+'

    // --- Dati e Riferimenti ---
    private Color coloreSelezionato = new Color(255, 255, 153); // Default Giallo

    // Palette di colori pastello per la selezione
    private final Color[] coloriPastello = {
            new Color(255, 255, 255), new Color(255, 250, 205), new Color(255, 255, 153), new Color(255, 229, 204),
            new Color(255, 218, 185), new Color(255, 182, 193), new Color(255, 204, 255), new Color(221, 160, 221),
            new Color(230, 230, 250), new Color(204, 229, 255), new Color(173, 216, 230), new Color(204, 255, 255),
            new Color(204, 255, 204), new Color(152, 251, 152), new Color(240, 240, 240), new Color(211, 211, 211)
    };

    private Bacheca bachecaTargetModel; // Usato se stiamo creando un NUOVO ToDo
    private ToDo todoInModifica;        // Usato se stiamo modificando un ToDo ESISTENTE
    private ToDoCardPanel cardGrafica;  // Riferimento al pannello grafico (Post-it) per aggiornarlo
    private ControllerGui controller;

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // =================================================================================
    // COSTRUTTORI
    // =================================================================================

    /**
     * Costruttore per la creazione di un NUOVO ToDo.
     * La checklist viene disabilitata perché serve un ID dal database prima di poter aggiungere voci.
     *
     * @param parentFrame   La finestra principale (per centrare il dialog).
     * @param controller    Il controller GUI per gestire le azioni.
     * @param bachecaModel  La bacheca in cui verrà inserito il nuovo ToDo.
     */
    public ToDoEditorDialog(JFrame parentFrame, ControllerGui controller, Bacheca bachecaModel) {
        this(parentFrame, controller); // Chiama il costruttore base per la UI
        this.bachecaTargetModel = bachecaModel;
        setTitle("Nuovo ToDo in " + bachecaModel.getTitolo());

        // Nascondiamo elementi non utilizzabili in fase di creazione
        chkCompletato.setVisible(false);
        btnElimina.setVisible(false);

        disabilitaChecklistNuovoToDo();
    }

    /**
     * Costruttore per la MODIFICA di un ToDo esistente.
     * Popola i campi con i dati attuali e carica la checklist.
     *
     * @param parentFrame La finestra principale.
     * @param controller  Il controller GUI.
     * @param card        Il pannello grafico (Post-it) da aggiornare visivamente.
     * @param todoModel   Il modello dati del ToDo da modificare.
     */
    public ToDoEditorDialog(JFrame parentFrame, ControllerGui controller, ToDoCardPanel card, ToDo todoModel) {
        this(parentFrame, controller); // Chiama il costruttore base per la UI
        this.cardGrafica = card;
        this.todoInModifica = todoModel;
        setTitle("Modifica: " + todoModel.getTitolo());

        // Popolamento campi dati
        txtTitolo.setText(todoModel.getTitolo());
        txtDescrizione.setText(todoModel.getDescrizione());

        if (todoModel.getDatescadenza() != null) {
            txtData.setText(todoModel.getDatescadenza().format(dtf));
        }

        if (todoModel.getColor() != null) {
            this.coloreSelezionato = todoModel.getColor();
            btnColore.setBackground(coloreSelezionato);
        }

        chkCompletato.setSelected(Boolean.TRUE.equals(todoModel.getCompletato()));

        btnElimina.setVisible(true);

        // Caricamento visivo della checklist
        popolaPannelloChecklist();
    }

    /**
     * Costruttore privato di base.
     * Inizializza l'interfaccia grafica comune (Layout, Pannelli, Bottoni).
     *
     * @param parentFrame La finestra padre.
     * @param controller  Il controller.
     */
    private ToDoEditorDialog(JFrame parentFrame, ControllerGui controller) {
        super(parentFrame, "Editor ToDo", true); // true = modale (blocca la finestra sotto)
        this.controller = controller;
        setSize(450, 650);
        setLocationRelativeTo(parentFrame);
        setLayout(new BorderLayout());

        // 1. PANNELLO SUPERIORE (Form Dati)
        JPanel mainForm = new JPanel(new GridBagLayout());
        mainForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1.0;

        addLabelAndComponent(mainForm, gbc, 0, "Titolo:", txtTitolo = new JTextField());
        addLabelAndComponent(mainForm, gbc, 1, "Descrizione:", new JScrollPane(txtDescrizione = new JTextArea(3, 20)));
        addLabelAndComponent(mainForm, gbc, 2, "Scadenza (gg/mm/aaaa):", txtData = new JTextField());

        chkCompletato = new JCheckBox("Completato");
        gbc.gridx = 1; gbc.gridy = 3;
        mainForm.add(chkCompletato, gbc);

        btnColore = new JButton(" ");
        btnColore.setOpaque(true);
        btnColore.setBorderPainted(false);
        btnColore.setBackground(coloreSelezionato);
        btnColore.addActionListener(e -> apriSelettorePastello());
        addLabelAndComponent(mainForm, gbc, 4, "Colore Sfondo:", btnColore);

        add(mainForm, BorderLayout.NORTH);

        // 2. PANNELLO CENTRALE (Checklist)
        JPanel checklistSection = setupChecklistSection();
        add(checklistSection, BorderLayout.CENTER);

        // 3. PANNELLO INFERIORE (Bottoni Azione)
        JPanel btnPanel = new JPanel();
        btnElimina = new JButton("Elimina ToDo");
        btnElimina.setBackground(new Color(255, 102, 102));
        btnElimina.setForeground(Color.WHITE);
        btnElimina.addActionListener(e -> gestisciEliminazione());

        btnSalva = new JButton("Salva");
        btnSalva.addActionListener(e -> gestisciSalvataggio());

        JButton btnAnnulla = new JButton("Annulla");
        btnAnnulla.addActionListener(e -> dispose());

        btnPanel.add(btnElimina);
        btnPanel.add(Box.createHorizontalStrut(20));
        btnPanel.add(btnSalva);
        btnPanel.add(btnAnnulla);

        add(btnPanel, BorderLayout.SOUTH);
    }

    // =================================================================================
    // METODI UI CHECKLIST
    // =================================================================================

    /**
     * Configura la sezione grafica della checklist (Input testo + Lista scrollabile).
     */
    private JPanel setupChecklistSection() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBorder(BorderFactory.createTitledBorder("Checklist Attività"));

        // Pannello input (Casella testo + Tasto Aggiungi)
        JPanel inputPanel = new JPanel(new BorderLayout(5, 0));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        txtNuovaAttivita = new JTextField();
        txtNuovaAttivita.addActionListener(e -> azioneAggiungiVoceChecklist()); // Invio = Aggiungi

        btnAddAttivita = new JButton("+");
        btnAddAttivita.addActionListener(e -> azioneAggiungiVoceChecklist());

        inputPanel.add(txtNuovaAttivita, BorderLayout.CENTER);
        inputPanel.add(btnAddAttivita, BorderLayout.EAST);
        wrapper.add(inputPanel, BorderLayout.NORTH);

        // Pannello Lista (Dentro uno ScrollPane)
        pnlCheckListContainer = new JPanel();
        pnlCheckListContainer.setLayout(new BoxLayout(pnlCheckListContainer, BoxLayout.Y_AXIS));

        // Wrapper per allineare gli elementi in alto
        JPanel alignContainer = new JPanel(new BorderLayout());
        alignContainer.add(pnlCheckListContainer, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(alignContainer);
        scroll.setBorder(null);
        wrapper.add(scroll, BorderLayout.CENTER);

        return wrapper;
    }

    /**
     * Disabilita l'input della checklist.
     * Usato quando si crea un nuovo ToDo che non ha ancora un ID nel database.
     */
    private void disabilitaChecklistNuovoToDo() {
        txtNuovaAttivita.setEnabled(false);
        btnAddAttivita.setEnabled(false);
        txtNuovaAttivita.setText("Salva il ToDo prima di aggiungere la checklist.");
    }

    /**
     * Svuota e ricostruisce la lista grafica della checklist basandosi sul Modello.
     */
    private void popolaPannelloChecklist() {
        pnlCheckListContainer.removeAll();
        if (todoInModifica != null && todoInModifica.getChecklist() != null) {
            for (CheckList item : todoInModifica.getChecklist()) {
                aggiungiRigaGrafica(item);
            }
        }
        pnlCheckListContainer.revalidate();
        pnlCheckListContainer.repaint();
    }

    /**
     * Gestisce l'aggiunta di una nuova voce:
     * 1. Chiama il controller per salvare nel DB.
     * 2. Aggiorna l'interfaccia aggiungendo la riga.
     * 3. Pulisce il campo input.
     */
    private void azioneAggiungiVoceChecklist() {
        String testo = txtNuovaAttivita.getText().trim();
        if (!testo.isEmpty()) {
            // 1. Salva tramite Controller
            controller.handleAggiungiCheckList(todoInModifica, testo);

            // 2. Recupera l'elemento appena aggiunto (è l'ultimo della lista)
            List<CheckList> lista = todoInModifica.getChecklist();
            if (!lista.isEmpty()) {
                CheckList ultimo = lista.get(lista.size() - 1);
                aggiungiRigaGrafica(ultimo);
            }

            // 3. Aggiorna stato globale (es. se riapro un todo completato aggiungendo una voce)
            aggiornaStatoVisivo();

            txtNuovaAttivita.setText("");
            pnlCheckListContainer.revalidate();
            pnlCheckListContainer.repaint();
        }
    }

    /**
     * Crea e aggiunge una singola riga grafica per un'attività della checklist.
     * Ogni riga contiene una Checkbox e un bottone Elimina.
     *
     * @param item L'oggetto CheckList da visualizzare.
     */
    private void aggiungiRigaGrafica(CheckList item) {
        JPanel riga = new JPanel(new BorderLayout(5, 0));
        riga.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        riga.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));

        // Checkbox attività
        JCheckBox chk = new JCheckBox(item.getNome());
        chk.setSelected(Boolean.TRUE.equals(item.getStato()));

        // Listener: Quando clicco la checkbox
        chk.addActionListener(e -> {
            // 1. Aggiorna DB e Modello tramite Controller
            controller.handleUpdateStatoCheckList(item, chk.isSelected(), todoInModifica);

            // 2. FIX IMPORTANTE: Aggiorna immediatamente lo stato visivo "Completato"
            // Se tutte le voci sono spuntate, il ToDo diventa verde subito.
            aggiornaStatoVisivo();
        });

        // Bottone Elimina (x rossa)
        JButton btnDel = new JButton("x");
        btnDel.setMargin(new Insets(0, 4, 0, 4));
        btnDel.setForeground(Color.RED);
        btnDel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        btnDel.setFocusPainted(false);
        btnDel.setContentAreaFilled(false);

        btnDel.addActionListener(e -> {
            int conf = JOptionPane.showConfirmDialog(this, "Eliminare voce?", "Conferma", JOptionPane.YES_NO_OPTION);
            if (conf == JOptionPane.YES_OPTION) {
                controller.handleEliminaCheckList(item, todoInModifica);
                pnlCheckListContainer.remove(riga);

                // Aggiorna stato globale dopo eliminazione (potrebbe completarsi il ToDo)
                aggiornaStatoVisivo();

                pnlCheckListContainer.revalidate();
                pnlCheckListContainer.repaint();
            }
        });

        riga.add(chk, BorderLayout.CENTER);
        riga.add(btnDel, BorderLayout.EAST);
        pnlCheckListContainer.add(riga);
    }

    /**
     * Sincronizza l'interfaccia grafica con lo stato reale del Modello.
     * Se la logica di business ha deciso che il ToDo è completato (perché tutte le voci sono fatte),
     * questo metodo spunta la checkbox principale e aggiorna il colore della card (post-it).
     */
    private void aggiornaStatoVisivo() {
        if (todoInModifica != null) {
            // 1. Aggiorna la checkbox "Completato" in questo Dialog
            chkCompletato.setSelected(Boolean.TRUE.equals(todoInModifica.getCompletato()));

            // 2. Aggiorna il colore del Post-it nella Dashboard (se visibile)
            if (cardGrafica != null) {
                cardGrafica.aggiornaGrafica();
            }
        }
    }

    // =================================================================================
    // METODI GESTIONE SALVATAGGIO & ELIMINAZIONE
    // =================================================================================

    /**
     * Raccoglie i dati dal form, valida la data e chiama il controller per salvare.
     */
    private void gestisciSalvataggio() {
        try {
            String titolo = txtTitolo.getText().trim();
            if (titolo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Il titolo è obbligatorio.", "Errore", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String dataStr = txtData.getText().trim();
            LocalDate scadenza = null;
            if (!dataStr.isEmpty()) {
                try {
                    scadenza = LocalDate.parse(dataStr, dtf);
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(this, "Formato data errato (usa gg/mm/aaaa).", "Errore Data", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            // Distinguiamo tra Modifica e Nuovo inserimento
            if (todoInModifica != null) {
                controller.handleSalvaModifica(todoInModifica, cardGrafica, titolo, txtDescrizione.getText(),
                        scadenza, coloreSelezionato, chkCompletato.isSelected());
            } else if (bachecaTargetModel != null) {
                controller.handleSalvaNuovoToDo(titolo, dataStr, txtDescrizione.getText(), coloreSelezionato, bachecaTargetModel);
            }
            dispose(); // Chiude la finestra
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Errore durante il salvataggio: " + e.getMessage());
        }
    }

    /**
     * Chiede conferma e chiama il controller per eliminare il ToDo.
     */
    private void gestisciEliminazione() {
        int conf = JOptionPane.showConfirmDialog(this, "Eliminare definitivamente il ToDo?", "Conferma Eliminazione", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            controller.handleEliminaToDo(todoInModifica, cardGrafica);
            dispose();
        }
    }

    // =================================================================================
    // METODI UTILITY LAYOUT
    // =================================================================================

    /**
     * Helper per aggiungere una Label e un Componente in una riga del GridBagLayout.
     */
    private void addLabelAndComponent(JPanel p, GridBagConstraints gbc, int row, String text, Component comp) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        p.add(new JLabel(text), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        p.add(comp, gbc);
    }

    /**
     * Apre una finestra modale con una griglia di colori pastello per la scelta.
     */
    private void apriSelettorePastello() {
        JDialog d = new JDialog(this, "Scegli Colore", true);
        d.setSize(350, 220);
        d.setLocationRelativeTo(this);
        JPanel p = new JPanel(new GridLayout(4, 4, 5, 5));

        for (Color c : coloriPastello) {
            JButton b = new JButton();
            b.setBackground(c);
            b.setOpaque(true);
            b.setBorderPainted(false);
            b.addActionListener(ev -> {
                coloreSelezionato = c;
                btnColore.setBackground(c);
                d.dispose();
            });
            p.add(b);
        }
        d.add(p);
        d.setVisible(true);
    }
}