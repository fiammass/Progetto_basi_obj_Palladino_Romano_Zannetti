package gui;

import controller.ControllerGui;
import model.ToDo;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BoardPanel extends JPanel {

    private JPanel containerCards;
    private String titoloBacheca;
    private JButton btnAdd;

    // Riferimento al controller da passare alle card figlie
    private ControllerGui controllerRef;

    public BoardPanel(String titolo) {
        this.titoloBacheca = titolo;

        setLayout(new BorderLayout());
        setTitoloBacheca(titolo);

        containerCards = new JPanel();
        containerCards.setLayout(new BoxLayout(containerCards, BoxLayout.Y_AXIS));

        // Aggiungiamo uno ScrollPane per gestire molte card
        JScrollPane scroll = new JScrollPane(containerCards);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(10);

        add(scroll, BorderLayout.CENTER);

        this.btnAdd = new JButton("+ Aggiungi");
        add(this.btnAdd, BorderLayout.SOUTH);
    }

    public void addListener(ControllerGui controller){
        this.controllerRef = controller; // Salviamo il controller!
        btnAdd.addActionListener(e -> {
            controller.handleAggiungiToDo(this);
        });
    }

    public void svuotaCard() {
        containerCards.removeAll();
        containerCards.revalidate();
        containerCards.repaint();
    }

    /**
     * Crea una nuova card grafica partendo dall'oggetto ToDo completo
     */
    public void aggiungiCard(ToDo todo) {
        if (controllerRef == null) {
            System.err.println("WARN: Aggiunta card senza controller in BoardPanel: " + titoloBacheca);
        }

        // Creiamo la card passando Controller e Model
        ToDoCardPanel card = new ToDoCardPanel(controllerRef, todo);

        containerCards.add(Box.createVerticalStrut(10));
        containerCards.add(card);

        containerCards.revalidate();
        containerCards.repaint();
    }

    public void setTitoloBacheca(String nuovoTitolo) {
        this.titoloBacheca = nuovoTitolo;
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                titoloBacheca.toUpperCase()
        ));
    }

    public String getTitolo() {
        return titoloBacheca;
    }

    // Metodo utile per debug o gestione massiva
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