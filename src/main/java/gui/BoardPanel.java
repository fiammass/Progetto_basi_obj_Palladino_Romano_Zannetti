package gui;


import controller.ControllerGui;
import javax.swing.*;
import java.awt.*;
import java.util.List;


public class BoardPanel extends JPanel {

    private JPanel containerCards;
    private String titoloBacheca; // Salviamo il titolo in una variabile
    private JButton btnAdd;

    public BoardPanel(String titolo) {
        this.titoloBacheca = titolo; // Memorizziamo il titolo

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                titoloBacheca.toUpperCase()
        ));

        containerCards = new JPanel();
        containerCards.setLayout(new BoxLayout(containerCards, BoxLayout.Y_AXIS));

        add(containerCards, BorderLayout.NORTH);

         this.btnAdd = new JButton("+ Aggiungi");
        add(this.btnAdd, BorderLayout.SOUTH);
    }
    // FIX: Metodo addListener spostato all'esterno del costruttore
    public void addListener(ControllerGui controller){
        btnAdd.addActionListener(e -> {
            controller.handleAggiungiToDo(this);
        });
    }

    public void setTitoloBacheca(String nuovoTitolo) {
        this.titoloBacheca = nuovoTitolo;
        // Aggiorna la grafica del bordo
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                titoloBacheca.toUpperCase()
        ));
    }

    public void svuotaCard() {
        containerCards.removeAll();
        containerCards.revalidate();
        containerCards.repaint();
    }

    // Metodo necessario per far funzionare il titolo nella finestra di dialogo
    public String getTitolo() {
        return titoloBacheca;
    }

    public void aggiungiCard(String titolo, String data, Color colore) {
        ToDoCardPanel card = new ToDoCardPanel(titolo, data, colore);
        containerCards.add(Box.createVerticalStrut(10));
        containerCards.add(card);

        // Forza l'aggiornamento visivo immediato
        containerCards.revalidate();
        containerCards.repaint();
    }

    // Metodo che restituisce la lista di tutte le card grafiche presenti in questa colonna
    public java.util.List<ToDoCardPanel> getCards() {
        java.util.List<ToDoCardPanel> lista = new java.util.ArrayList<>();

        // Scorre tutti i componenti dentro il container
        for (Component c : containerCards.getComponents()) {
            // Se il componente è una Card (e non lo spazio vuoto), lo aggiungo
            if (c instanceof ToDoCardPanel) {
                lista.add((ToDoCardPanel) c);
            }
        }
        return lista;
    }
}