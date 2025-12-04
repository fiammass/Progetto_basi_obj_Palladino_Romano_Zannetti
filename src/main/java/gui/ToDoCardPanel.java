package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import controller.ControllerGui;

public class ToDoCardPanel extends JPanel {

    // Salviamo i componenti come variabili per poterli modificare dopo
    private JLabel lblTitolo;
    private JLabel lblScadenza;

    // Salviamo anche i dati grezzi
    private String titolo;
    private String scadenza;
    private Color colore;

    private ControllerGui controller;

    public ToDoCardPanel(String titolo, String scadenza, Color coloreSfondo, ControllerGui controller) {
        // ASSUNZIONE: Se il tuo Controller è già disponibile quando crei la card,
        // è meglio passarlo al costruttore.
        this(titolo, scadenza, coloreSfondo); // Chiama il costruttore esistente
        this.controller = controller;
    }

    public ToDoCardPanel(String titolo, String scadenza, Color coloreSfondo) {
        this.titolo = titolo;
        this.scadenza = scadenza;
        this.colore = coloreSfondo;

        setLayout(new BorderLayout());
        setBackground(coloreSfondo);
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        // Inizializziamo le Label
        lblTitolo = new JLabel(titolo);
        lblTitolo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitolo.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));

        lblScadenza = new JLabel("Scad: " + scadenza);
        lblScadenza.setFont(new Font("Arial", Font.ITALIC, 11));
        lblScadenza.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));

        add(lblTitolo, BorderLayout.CENTER);
        add(lblScadenza, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(250, 60));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // ASCOLTATORE CLICK: DELEGA al Controller
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Il Controller deve essere responsabile di aprire l'Editor.
                if (controller != null) {
                    controller.handleModificaToDo(ToDoCardPanel.this); // Delega l'azione
                } else {
                    System.err.println("Errore: Controller non impostato sulla card.");
                    // Logica alternativa se il controller è null
                }
            }
        });
    }

    public void setController(ControllerGui controller) {
        this.controller = controller;
    }

    // METODO: Serve all'Editor per aggiornare questa card
    public void aggiornaDati(String nuovoTitolo, String nuovaScadenza, Color nuovoColore, boolean completato) {
        this.titolo = nuovoTitolo;
        this.scadenza = nuovaScadenza;
        this.colore = nuovoColore;

        // Aggiorniamo la grafica
        lblTitolo.setText(nuovoTitolo);
        lblScadenza.setText("Scad: " + nuovaScadenza);
        setBackground(nuovoColore);

        // Se è completato, cambiamo il bordo in verde altrimenti è rosso
        if(completato) {
            lblTitolo.setForeground(Color.GREEN); // Titolo verde se fatto
        } else {
            lblTitolo.setForeground(Color.RED); // Titolo rosso se non è stato fatto
        }

        revalidate();
        repaint();
    }

    // Getter utili per l'editor
    public String getTitolo() { return titolo; }
    public String getScadenza() { return scadenza; }
    public Color getColore() { return colore; }
}