import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel {

    private JPanel containerCards;
    private String titoloBacheca; // Salviamo il titolo in una variabile

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

        JButton btnAdd = new JButton("+ Aggiungi");
        add(btnAdd, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> {
            // 1. Trovo la finestra principale
            JFrame dashboard = (JFrame) SwingUtilities.getWindowAncestor(this);

            // 2. Apro l'editor passando "this" (cioè QUESTA bacheca specifica)
            // Così l'editor saprà dove aggiungere il nuovo ToDo
            new ToDoEditorDialog(dashboard, this).setVisible(true);
        });
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