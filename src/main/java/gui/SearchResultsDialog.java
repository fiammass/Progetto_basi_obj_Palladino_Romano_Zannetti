package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List; // IMPORTANTE

/**
 * RAppresentazione della ricerca del todo
 */
public class SearchResultsDialog extends JDialog {

    /**
     * Costruttore della calsse searchResultDialog
      * @param parentFrame
     * @param titoloFinestra
     * @param risultati
     * @param nomeBacheca
     */
    public SearchResultsDialog(JFrame parentFrame, String titoloFinestra, List<ToDoCardPanel> risultati, String nomeBacheca) {
        super(parentFrame, titoloFinestra, true);
        setSize(600, 400);
        setLocationRelativeTo(parentFrame);
        setLayout(new BorderLayout());

        // Titolo
        JLabel lblTitolo = new JLabel(titoloFinestra);
        lblTitolo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitolo.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTitolo, BorderLayout.NORTH);

        // Colonne Tabella
        String[] colonne = {"Titolo", "Scadenza", "Bacheca"};

        DefaultTableModel model = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        // RIEMPIAMO LA TABELLA
        // Se la lista è null (per sicurezza), creiamo lista vuota
        if (risultati != null) {
            for (ToDoCardPanel card : risultati) {
                model.addRow(new Object[]{
                        card.getTitolo(),
                        card.getScadenza(),
                        nomeBacheca
                });
            }
        }

        JTable table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnChiudi = new JButton("Chiudi");
        btnChiudi.addActionListener(e -> dispose());
        add(btnChiudi, BorderLayout.SOUTH);
    }
}