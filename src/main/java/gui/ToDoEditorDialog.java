package gui;

import controller.ControllerGui;
import model.Bacheca;
import model.CheckList;
import model.ToDo;
import model.Utente;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Finestra di dialogo modale per la gestione completa di un ToDo.
 * <p>
 * Questa classe utilizza un {@link JTabbedPane} per organizzare le funzionalità in tre schede:
 * <ul>
 * <li><b>Dettagli:</b> Modifica di titolo, descrizione, data, colore e stato di completamento.</li>
 * <li><b>Checklist:</b> Gestione delle sotto-attività (aggiunta, rimozione, spunta).</li>
 * <li><b>Condivisioni:</b> Gestione degli utenti con cui il ToDo è condiviso.</li>
 * </ul>
 * Gestisce inoltre i permessi: se l'utente corrente non è l'autore del ToDo, alcune funzionalità
 * (come la condivisione o l'eliminazione) vengono disabilitate.
 */
public class ToDoEditorDialog extends JDialog {

    private JTextField txtTitolo, txtData;
    private JTextArea txtDescrizione;
    private JCheckBox chkCompletato;
    private JButton btnColore, btnSalva, btnElimina;

    private JPanel pnlCheckListContainer;
    private JTextField txtNuovaAttivita;
    private JButton btnAddAttivita;

    private DefaultListModel<String> listModelCondivisioni;
    private JList<String> listCondivisioni;
    private JTextField txtUtenteCondivisione;
    private JButton btnAddCondivisione, btnRemoveCondivisione;

    private Color coloreSelezionato = new Color(255, 255, 153);
    private final Color[] coloriPastello = { new Color(255, 255, 153), new Color(204, 255, 204), new Color(255, 102, 102), new Color(204, 229, 255) };

    private ToDo todoInModifica;
    private Bacheca bachecaTarget;
    private ControllerGui controller;
    private ToDoCardPanel cardRef;
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Costruttore per la MODIFICA di un ToDo esistente.
     * Carica i dati del ToDo, popola le liste (checklist e condivisioni) e verifica i permessi dell'utente.
     *
     * @param parent Il frame genitore.
     * @param ctrl   Il controller GUI.
     * @param card   Il pannello grafico (card) da aggiornare visivamente dopo le modifiche.
     * @param todo   L'oggetto ToDo da modificare.
     */
    public ToDoEditorDialog(JFrame parent, ControllerGui ctrl, ToDoCardPanel card, ToDo todo) {
        super(parent, "Modifica ToDo: " + todo.getTitolo(), true);
        this.controller = ctrl;
        this.todoInModifica = todo;
        this.cardRef = card;
        initUI();
        popolaCampi();
    }

    /**
     * Costruttore per la CREAZIONE di un nuovo ToDo.
     * Disabilita le funzionalità avanzate (Checklist e Condivisioni) poiché il ToDo
     * deve essere prima salvato nel database per ottenere un ID univoco.
     *
     * @param parent  Il frame genitore.
     * @param ctrl    Il controller GUI.
     * @param bacheca La bacheca in cui inserire il nuovo ToDo.
     */
    public ToDoEditorDialog(JFrame parent, ControllerGui ctrl, Bacheca bacheca) {
        super(parent, "Nuovo ToDo", true);
        this.controller = ctrl;
        this.bachecaTarget = bacheca;
        initUI();
        disabilitaFunzioniNuovo();
    }

    /**
     * Inizializza l'interfaccia grafica, configurando il layout a schede (Tabs)
     * e i pannelli principali.
     */
    private void initUI() {
        setSize(500, 650);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Dettagli", createDettagliPanel());
        tabs.addTab("Checklist", createChecklistPanel());
        tabs.addTab("Condivisioni", createCondivisioniPanel());

        add(tabs, BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    /**
     * Crea il pannello per la modifica dei dettagli principali del ToDo.
     *
     * @return Il pannello configurato.
     */
    private JPanel createDettagliPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.insets = new Insets(5,5,5,5); gbc.weightx=1.0;

        addC(p, gbc, 0, "Titolo:", txtTitolo = new JTextField());
        addC(p, gbc, 1, "Descrizione:", new JScrollPane(txtDescrizione = new JTextArea(3, 20)));
        addC(p, gbc, 2, "Scadenza:", txtData = new JTextField());

        chkCompletato = new JCheckBox("Completato");
        gbc.gridx=1; gbc.gridy=3; p.add(chkCompletato, gbc);

        btnColore = new JButton(" "); btnColore.setOpaque(true); btnColore.setBorderPainted(false);
        btnColore.setBackground(coloreSelezionato);
        btnColore.addActionListener(e -> apriSelettorePastello());
        addC(p, gbc, 4, "Colore:", btnColore);

        return p;
    }

    /**
     * Crea il pannello per la gestione delle condivisioni con altri utenti.
     *
     * @return Il pannello configurato.
     */
    private JPanel createCondivisioniPanel() {
        JPanel p = new JPanel(new BorderLayout(5,5));
        p.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        listModelCondivisioni = new DefaultListModel<>();
        listCondivisioni = new JList<>(listModelCondivisioni);
        p.add(new JScrollPane(listCondivisioni), BorderLayout.CENTER);

        JPanel bot = new JPanel(new BorderLayout());
        txtUtenteCondivisione = new JTextField();
        JPanel btns = new JPanel(new GridLayout(1,2));
        btnAddCondivisione = new JButton("Condividi");
        btnRemoveCondivisione = new JButton("Rimuovi");
        btns.add(btnAddCondivisione); btns.add(btnRemoveCondivisione);

        bot.add(new JLabel("Username: "), BorderLayout.WEST);
        bot.add(txtUtenteCondivisione, BorderLayout.CENTER);
        bot.add(btns, BorderLayout.SOUTH);
        p.add(bot, BorderLayout.SOUTH);

        btnAddCondivisione.addActionListener(e -> {
            String u = txtUtenteCondivisione.getText().trim();
            if(!u.isEmpty() && todoInModifica!=null) {
                controller.handleAggiungiCondivisione(todoInModifica, u);
                popolaListe();
                txtUtenteCondivisione.setText("");
            }
        });

        btnRemoveCondivisione.addActionListener(e -> {
            String u = listCondivisioni.getSelectedValue();
            if(u!=null && todoInModifica!=null) {
                controller.handleRimuoviCondivisione(todoInModifica, u);
                popolaListe();
            }
        });

        return p;
    }

    /**
     * Crea il pannello per la gestione della checklist.
     *
     * @return Il pannello configurato.
     */
    private JPanel createChecklistPanel() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JPanel inputPanel = new JPanel(new BorderLayout(5, 0));
        txtNuovaAttivita = new JTextField();
        btnAddAttivita = new JButton("+");
        btnAddAttivita.addActionListener(e -> azioneAggiungiVoceChecklist());
        inputPanel.add(txtNuovaAttivita, BorderLayout.CENTER);
        inputPanel.add(btnAddAttivita, BorderLayout.EAST);
        wrapper.add(inputPanel, BorderLayout.NORTH);

        pnlCheckListContainer = new JPanel();
        pnlCheckListContainer.setLayout(new BoxLayout(pnlCheckListContainer, BoxLayout.Y_AXIS));

        JPanel align = new JPanel(new BorderLayout());
        align.add(pnlCheckListContainer, BorderLayout.NORTH);

        wrapper.add(new JScrollPane(align), BorderLayout.CENTER);
        return wrapper;
    }

    /**
     * Popola i campi del form con i dati del ToDo in modifica.
     * Inizializza anche le liste e verifica i permessi.
     */
    private void popolaCampi() {
        if(todoInModifica == null) return;
        txtTitolo.setText(todoInModifica.getTitolo());
        txtDescrizione.setText(todoInModifica.getDescrizione());
        if(todoInModifica.getDatescadenza()!=null) txtData.setText(todoInModifica.getDatescadenza().format(dtf));
        chkCompletato.setSelected(Boolean.TRUE.equals(todoInModifica.getCompletato()));
        coloreSelezionato = todoInModifica.getColor();
        btnColore.setBackground(coloreSelezionato);

        popolaListe();
        popolaPannelloChecklist();
        checkPermessi();
    }

    /**
     * Aggiorna la lista visiva degli utenti con cui è condiviso il ToDo.
     */
    private void popolaListe() {
        listModelCondivisioni.clear();
        if(todoInModifica != null && todoInModifica.getCondivisioni() != null) {
            for(Utente u : todoInModifica.getCondivisioni()) {
                listModelCondivisioni.addElement(u.getlogin());
            }
        }
    }

    /**
     * Ricostruisce il pannello della checklist in base ai dati del modello.
     */
    private void popolaPannelloChecklist() {
        pnlCheckListContainer.removeAll();
        if (todoInModifica != null && todoInModifica.getChecklist() != null) {
            for (CheckList item : todoInModifica.getChecklist()) {
                aggiungiRigaGrafica(item);
            }
        }
        pnlCheckListContainer.revalidate(); pnlCheckListContainer.repaint();
    }

    /**
     * Gestisce l'azione di aggiunta di una nuova voce alla checklist.
     */
    private void azioneAggiungiVoceChecklist() {
        String testo = txtNuovaAttivita.getText().trim();
        if (!testo.isEmpty()) {
            controller.handleAggiungiCheckList(todoInModifica, testo);
            popolaPannelloChecklist();
            txtNuovaAttivita.setText("");
        }
    }

    /**
     * Aggiunge una singola riga grafica (Checkbox + Bottone Elimina) al pannello della checklist.
     *
     * @param item L'elemento della checklist da visualizzare.
     */
    private void aggiungiRigaGrafica(CheckList item) {
        JPanel riga = new JPanel(new BorderLayout());
        JCheckBox chk = new JCheckBox(item.getNome());
        chk.setSelected(Boolean.TRUE.equals(item.getStato()));
        chk.addActionListener(e -> {
            controller.handleUpdateStatoCheckList(item, chk.isSelected(), todoInModifica);
            chkCompletato.setSelected(Boolean.TRUE.equals(todoInModifica.getCompletato()));
            if(cardRef!=null) cardRef.aggiornaGrafica();
        });
        JButton btnDel = new JButton("x");
        btnDel.setForeground(Color.RED);
        btnDel.addActionListener(e -> {
            controller.handleEliminaCheckList(item, todoInModifica);
            popolaPannelloChecklist();
            chkCompletato.setSelected(Boolean.TRUE.equals(todoInModifica.getCompletato()));
            if(cardRef!=null) cardRef.aggiornaGrafica();
        });
        riga.add(chk, BorderLayout.CENTER); riga.add(btnDel, BorderLayout.EAST);
        pnlCheckListContainer.add(riga);
    }

    /**
     * Disabilita i controlli per le funzionalità che richiedono un ToDo già salvato
     * (Checklist e Condivisioni) durante la creazione di un nuovo ToDo.
     */
    private void disabilitaFunzioniNuovo() {
        txtNuovaAttivita.setEnabled(false);
        btnAddAttivita.setEnabled(false);
        txtUtenteCondivisione.setEnabled(false);
        btnAddCondivisione.setEnabled(false);
        btnRemoveCondivisione.setEnabled(false);
        txtNuovaAttivita.setText("Salva il ToDo prima di usare checklist o condivisioni.");
    }

    /**
     * Verifica se l'utente corrente è il proprietario del ToDo.
     * In caso negativo, disabilita le funzionalità di gestione condivisione ed eliminazione.
     */
    private void checkPermessi() {
        if(todoInModifica != null && !todoInModifica.getAutore().getlogin().equals(controller.getUsernameCorrente())) {
            btnAddCondivisione.setEnabled(false);
            btnRemoveCondivisione.setEnabled(false);
            btnElimina.setEnabled(false);
            txtUtenteCondivisione.setEnabled(false);
        }
    }

    /**
     * Crea il pannello inferiore contenente i pulsanti di azione (Salva, Elimina, Annulla).
     *
     * @return Il pannello configurato.
     */
    private JPanel createButtonPanel() {
        JPanel p = new JPanel();
        btnElimina = new JButton("Elimina");
        btnElimina.setBackground(Color.RED); btnElimina.setForeground(Color.WHITE);
        btnElimina.addActionListener(e -> {
            controller.handleEliminaToDo(todoInModifica, cardRef);
            dispose();
        });

        btnSalva = new JButton("Salva");
        btnSalva.addActionListener(e -> gestisciSalvataggio());

        JButton btnAnnulla = new JButton("Annulla");
        btnAnnulla.addActionListener(e -> dispose());

        p.add(btnElimina); p.add(Box.createHorizontalStrut(20)); p.add(btnSalva); p.add(btnAnnulla);
        return p;
    }

    /**
     * Gestisce il salvataggio dei dati (Creazione o Modifica).
     * Effettua la validazione della data e delega l'operazione al controller.
     */
    private void gestisciSalvataggio() {
        try {
            String titolo = txtTitolo.getText().trim();
            if (titolo.isEmpty()) return;
            LocalDate scad = null;
            if(!txtData.getText().isEmpty()) scad = LocalDate.parse(txtData.getText(), dtf);

            if(todoInModifica != null) {
                controller.handleSalvaModifica(todoInModifica, cardRef, titolo, txtDescrizione.getText(), scad, coloreSelezionato, chkCompletato.isSelected());
            } else if(bachecaTarget != null) {
                controller.handleSalvaNuovoToDo(titolo, txtData.getText(), txtDescrizione.getText(), coloreSelezionato, bachecaTarget);
            }
            dispose();
        } catch(DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Data errata (gg/mm/aaaa)");
        }
    }

    /**
     * Metodo helper per aggiungere componenti al GridBagLayout.
     */
    private void addC(JPanel p, GridBagConstraints gbc, int y, String l, Component c) {
        gbc.gridx=0; gbc.gridy=y; gbc.weightx=0.3; p.add(new JLabel(l), gbc);
        gbc.gridx=1; gbc.weightx=0.7; p.add(c, gbc);
    }

    /**
     * Apre un selettore di colori pastello in una finestra modale.
     */
    private void apriSelettorePastello() {
        JDialog d = new JDialog(this, "Scegli Colore", true); d.setSize(300, 200); d.setLocationRelativeTo(this);
        JPanel p = new JPanel(new GridLayout(2, 3));
        for(Color c : coloriPastello) {
            JButton b = new JButton(); b.setBackground(c); b.setOpaque(true);
            b.addActionListener(ev -> { coloreSelezionato=c; btnColore.setBackground(c); d.dispose(); });
            p.add(b);
        }
        d.add(p); d.setVisible(true);
    }
}