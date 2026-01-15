package gui;

import model.ToDo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Finestra di dialogo modale utilizzata per visualizzare i risultati di una ricerca.
 * <p>
 * Questa classe mostra un elenco tabellare di ToDo, presentando per ciascuno:
 * <ul>
 * <li>Titolo dell'attività.</li>
 * <li>Data di scadenza (formattata).</li>
 * <li>Nome della bacheca di appartenenza.</li>
 * </ul>
 * I dati vengono presentati in una {@link JTable} non modificabile.
 */
public class SearchResultsDialog extends JDialog {

    /**
     * Costruttore della classe SearchResultsDialog.
     * Inizializza la finestra, configura la tabella per la visualizzazione dei dati
     * e la popola con la lista dei risultati forniti.
     *
     * @param parentFrame    La finestra principale (Dashboard) che funge da genitore per questa dialog modale.
     * @param titoloFinestra Il titolo da assegnare alla finestra di dialogo.
     * @param risultati      La lista dei pannelli ({@link ToDoCardPanel}) trovati dalla ricerca, da cui estrarre i dati.
     * @param nomeBacheca    Il nome della bacheca in cui sono stati trovati i risultati (per visualizzazione).
     */
    public SearchResultsDialog(JFrame parentFrame, String titoloFinestra, List<ToDoCardPanel> risultati, String nomeBacheca) {
        super(parentFrame, titoloFinestra, true);
        setSize(600, 400);
        setLocationRelativeTo(parentFrame);
        setLayout(new BorderLayout());

        JLabel lblTitolo = new JLabel(titoloFinestra);
        lblTitolo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitolo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitolo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(lblTitolo, BorderLayout.NORTH);

        String[] colonne = {"Titolo", "Scadenza", "Bacheca"};

        DefaultTableModel model = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        if (risultati != null) {
            for (ToDoCardPanel card : risultati) {
                ToDo todo = card.getToDo();

                String dataStr = "N/D";
                if (todo.getDatescadenza() != null) {
                    dataStr = todo.getDatescadenza().format(dtf);
                }

                model.addRow(new Object[]{
                        todo.getTitolo(),
                        dataStr,
                        nomeBacheca
                });
            }
        }

        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnChiudi = new JButton("Chiudi");
        btnChiudi.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnChiudi);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        add(buttonPanel, BorderLayout.SOUTH);
    }
}