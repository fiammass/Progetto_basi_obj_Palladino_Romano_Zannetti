package gui;

import controller.ControllerGui;
import model.ToDo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Pannello grafico che rappresenta un singolo ToDo sotto forma di "card" o "post-it".
 * <p>
 * Questo componente visualizza il titolo e la data di scadenza del ToDo.
 * Gestisce automaticamente il proprio colore di sfondo e lo stile del testo in base allo stato del ToDo:
 * <ul>
 * <li><b>Verde:</b> Attività completata.</li>
 * <li><b>Rosso:</b> Attività scaduta e non completata.</li>
 * <li><b>Colore Utente/Giallo:</b> Attività in corso.</li>
 * </ul>
 * Il pannello è cliccabile e apre la finestra di modifica tramite il {@link ControllerGui}.
 */
public class ToDoCardPanel extends JPanel {

    private JLabel lblTitolo;
    private JLabel lblScadenza;

    private ToDo todo;
    private ControllerGui controller;

    private static final Color COLORE_SCADUTO = new Color(255, 102, 102);
    private static final Color COLORE_COMPLETATO = new Color(144, 238, 144);
    private static final Color COLORE_DEFAULT = new Color(255, 255, 204);

    /**
     * Costruttore della classe ToDoCardPanel.
     * Inizializza l'interfaccia grafica e i listener per l'interazione.
     *
     * @param controller Il riferimento al controller per gestire il click sulla card.
     * @param todo       L'oggetto del modello dati da visualizzare.
     */
    public ToDoCardPanel(ControllerGui controller, ToDo todo) {
        this.controller = controller;
        this.todo = todo;

        initUI();
        initListeners();
    }

    /**
     * Inizializza i componenti grafici del pannello (Label, Bordi, Layout).
     * Imposta il pannello come opaco per permettere il rendering del colore di sfondo.
     */
    private void initUI() {
        setLayout(new BorderLayout());

        setOpaque(true);

        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        lblTitolo = new JLabel();
        lblTitolo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitolo.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));

        lblScadenza = new JLabel();
        lblScadenza.setFont(new Font("Arial", Font.ITALIC, 11));
        lblScadenza.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));

        add(lblTitolo, BorderLayout.CENTER);
        add(lblScadenza, BorderLayout.SOUTH);

        aggiornaGrafica();

        setPreferredSize(new Dimension(250, 60));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    /**
     * Configura i listener per gli eventi del mouse.
     * Un click sul pannello invoca il metodo di modifica nel controller.
     */
    private void initListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (controller != null) {
                    controller.handleModificaToDo(ToDoCardPanel.this);
                }
            }
        });
    }

    /**
     * Aggiorna l'aspetto visivo del pannello in base allo stato corrente del ToDo.
     * <p>
     * Logica di priorità dei colori:
     * <ol>
     * <li>Se completato: Sfondo Verde, testo barrato.</li>
     * <li>Se scaduto (e non completato): Sfondo Rosso, testo di avviso.</li>
     * <li>Altrimenti: Colore definito dall'utente o Giallo default.</li>
     * </ol>
     */
    public void aggiornaGrafica() {
        boolean completato = Boolean.TRUE.equals(todo.getCompletato());
        boolean scaduto = false;

        if (todo.getDatescadenza() != null &&
                todo.getDatescadenza().isBefore(LocalDate.now()) &&
                !completato) {
            scaduto = true;
        }

        if (completato) {
            setBackground(COLORE_COMPLETATO);
        } else if (scaduto) {
            setBackground(COLORE_SCADUTO);
        } else {
            if (todo.getColor() != null) {
                setBackground(todo.getColor());
            } else {
                setBackground(COLORE_DEFAULT);
            }
        }

        if (completato) {
            lblTitolo.setText("<html><strike>" + todo.getTitolo() + "</strike></html>");
            lblTitolo.setForeground(new Color(0, 100, 0));
        } else {
            lblTitolo.setText(todo.getTitolo());
            lblTitolo.setForeground(Color.BLACK);
        }

        if (todo.getDatescadenza() != null) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String dataText = "Scad: " + todo.getDatescadenza().format(dtf);

            if (scaduto) {
                lblScadenza.setText(dataText + " (SCADUTO!)");
                lblScadenza.setForeground(Color.WHITE);
            } else {
                lblScadenza.setText(dataText);
                lblScadenza.setForeground(Color.BLACK);
            }
        } else {
            lblScadenza.setText("Scad: --/--/----");
            lblScadenza.setForeground(Color.BLACK);
        }

        revalidate();
        repaint();
    }

    /**
     * Restituisce l'oggetto ToDo associato a questo pannello grafico.
     *
     * @return L'istanza di {@link ToDo}.
     */
    public ToDo getToDo() {
        return todo;
    }
}