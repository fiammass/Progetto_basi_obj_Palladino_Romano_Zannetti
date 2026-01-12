package gui;

import model.ToDo; // Fondamentale per accedere ai dati

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Rappresentazione della finestra con i risultati della ricerca.
 */
public class SearchResultsDialog extends JDialog {

    /**
     * Costruttore della classe SearchResultsDialog
     * @param parentFrame   La finestra principale (Dashboard)
     * @param titoloFinestra Il titolo della dialog
     * @param risultati     La lista delle card trovate
     * @param nomeBacheca   Il nome della bacheca (opzionale, per display)
     */
    public SearchResultsDialog(JFrame parentFrame, String titoloFinestra, List<ToDoCardPanel> risultati, String nomeBacheca) {
        super(parentFrame, titoloFinestra, true);
        setSize(600, 400);
        setLocationRelativeTo(parentFrame);
        setLayout(new BorderLayout());

        // 1. Titolo in alto
        JLabel lblTitolo = new JLabel(titoloFinestra);
        lblTitolo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitolo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitolo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(lblTitolo, BorderLayout.NORTH);

        // 2. Definizione Colonne della Tabella
        String[] colonne = {"Titolo", "Scadenza", "Bacheca"};

        // Creiamo il modello della tabella e rendiamo le celle non modificabili
        DefaultTableModel model = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        // Formattatore per le date (da LocalDate a Stringa leggibile)
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // 3. Riempimento della Tabella
        if (risultati != null) {
            for (ToDoCardPanel card : risultati) {
                // RECUPERO DATI CORRETTO:
                // Prima prendiamo l'oggetto ToDo intero dalla card
                ToDo todo = card.getToDo();

                // Gestiamo la data (evitiamo errori se è null)
                String dataStr = "N/D";
                if (todo.getDatescadenza() != null) {
                    dataStr = todo.getDatescadenza().format(dtf);
                }

                // Aggiungiamo la riga al modello della tabella
                model.addRow(new Object[]{
                        todo.getTitolo(), // Titolo dal model
                        dataStr,          // Data formattata
                        nomeBacheca       // Nome bacheca passato come parametro
                });
            }
        }

        // Creazione della JTable visiva
        JTable table = new JTable(model);
        table.setRowHeight(25); // Altezza righe per leggibilità
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

        // Aggiungiamo la tabella dentro uno ScrollPane (per le barre di scorrimento)
        add(new JScrollPane(table), BorderLayout.CENTER);

        // 4. Bottone Chiudi in basso
        JButton btnChiudi = new JButton("Chiudi");
        btnChiudi.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnChiudi);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        add(buttonPanel, BorderLayout.SOUTH);
    }
}