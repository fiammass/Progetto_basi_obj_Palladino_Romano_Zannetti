package gui;

import controller.ControllerGui;
import model.ToDo;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Pannello grafico che rappresenta una singola Bacheca (es. "Università", "Lavoro").
 * <p>
 * Questa classe estende {@link JPanel} e gestisce:
 * <ul>
 * <li>Il titolo della bacheca (visualizzato come bordo).</li>
 * <li>Un contenitore scrollabile per le card dei ToDo ({@link ToDoCardPanel}).</li>
 * <li>Un pulsante per aggiungere nuovi ToDo a questa specifica bacheca.</li>
 * </ul>
 */
public class BoardPanel extends JPanel {

    private JPanel containerCards;
    private String titoloBacheca;
    private JButton btnAdd;
    private ControllerGui controllerRef;

    /**
     * Costruttore della classe BoardPanel.
     * Inizializza il layout, lo scroll pane verticale e il pulsante di aggiunta.
     *
     * @param titolo Il titolo della bacheca da visualizzare nel bordo.
     */
    public BoardPanel(String titolo) {
        this.titoloBacheca = titolo;

        setLayout(new BorderLayout());
        setTitoloBacheca(titolo);

        containerCards = new JPanel();
        containerCards.setLayout(new BoxLayout(containerCards, BoxLayout.Y_AXIS));

        JScrollPane scroll = new JScrollPane(containerCards);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(10);

        add(scroll, BorderLayout.CENTER);

        this.btnAdd = new JButton("+ Aggiungi");
        add(this.btnAdd, BorderLayout.SOUTH);
    }

    /**
     * Registra il controller come listener per gli eventi di questo pannello.
     * In particolare, collega il pulsante "Aggiungi" al metodo del controller
     * per creare un nuovo ToDo.
     *
     * @param controller L'istanza del ControllerGui.
     */
    public void addListener(ControllerGui controller){
        this.controllerRef = controller;
        btnAdd.addActionListener(e -> {
            controller.handleAggiungiToDo(this);
        });
    }

    /**
     * Rimuove tutte le card attualmente visualizzate nella bacheca.
     * Utile per pulire la vista prima di ricaricare i dati aggiornati dal database.
     */
    public void svuotaCard() {
        containerCards.removeAll();
        containerCards.revalidate();
        containerCards.repaint();
    }

    /**
     * Crea una nuova card grafica ({@link ToDoCardPanel}) basata su un oggetto ToDo
     * e la aggiunge visivamente alla lista verticale di questa bacheca.
     *
     * @param todo L'oggetto modello ToDo da visualizzare.
     */
    public void aggiungiCard(ToDo todo) {
        if (controllerRef == null) {
            System.err.println("WARN: Aggiunta card senza controller in BoardPanel: " + titoloBacheca);
        }

        ToDoCardPanel card = new ToDoCardPanel(controllerRef, todo);

        containerCards.add(Box.createVerticalStrut(10));
        containerCards.add(card);

        containerCards.revalidate();
        containerCards.repaint();
    }

    /**
     * Imposta o aggiorna il titolo della bacheca, aggiornando anche il bordo grafico.
     *
     * @param nuovoTitolo Il nuovo titolo da assegnare.
     */
    public void setTitoloBacheca(String nuovoTitolo) {
        this.titoloBacheca = nuovoTitolo;
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                titoloBacheca.toUpperCase()
        ));
    }

    /**
     * Restituisce il titolo attuale della bacheca.
     *
     * @return La stringa del titolo.
     */
    public String getTitolo() {
        return titoloBacheca;
    }

    /**
     * Restituisce una lista di tutte le card grafiche attualmente presenti nel pannello.
     * Utile per operazioni di ricerca o debug.
     *
     * @return Una lista di oggetti {@link ToDoCardPanel}.
     */
    public List<ToDoCardPanel> getCards() {
        List<ToDoCardPanel> lista = new ArrayList<>();
        for (Component c : containerCards.getComponents()) {
            if (c instanceof ToDoCardPanel) {
                lista.add((ToDoCardPanel) c);
            }
        }
        return lista;
    }
}