import javax.swing.*;
import java.awt.*;

public class ToDoEditorDialog extends JDialog {

    private JTextField txtTitolo;
    private JTextArea txtDescrizione;
    private JTextField txtData;
    private JCheckBox chkCompletato;
    private JButton btnColore;

    // Default: Giallo Post-it
    private Color coloreSelezionato = new Color(255, 255, 153);

    private BoardPanel bachecaTarget;
    private ToDoCardPanel cardDaModificare;

    // Palette di colori pastello per una visualizzazione migliore del testo che andiamo a scrivere sopra
    private final Color[] coloriPastello = {
            // Gialli e Arancioni
            new Color(255, 255, 255), // Bianco Puro
            new Color(255, 250, 205), // Chiffon Limone (Bianco Sporco)
            new Color(255, 255, 153), // Giallo Classico
            new Color(255, 229, 204), // Arancione Chiaro

            // Rosa e Viola
            new Color(255, 218, 185), // Pesca
            new Color(255, 182, 193), // Rosa Antico
            new Color(255, 204, 255), // Rosa Confetto
            new Color(221, 160, 221), // Viola Prugna

            // Azzurri e Blu
            new Color(230, 230, 250), // Lavanda
            new Color(204, 229, 255), // Blu Lavanda
            new Color(173, 216, 230), // Azzurro Cielo
            new Color(204, 255, 255), // Ciano Ghiaccio

            // Verdi e Grigi
            new Color(204, 255, 204), // Verde Menta
            new Color(152, 251, 152), // Verde Pallido
            new Color(240, 240, 240), // Grigio Perla
            new Color(211, 211, 211)  // Grigio Chiaro
    };

    // Costruttore NUOVO
    public ToDoEditorDialog(JFrame parentFrame, BoardPanel bacheca) {
        this(parentFrame);
        this.bachecaTarget = bacheca;
        setTitle("Nuovo ToDo in " + bacheca.getTitolo());
    }

    // Costruttore MODIFICA
    public ToDoEditorDialog(JFrame parentFrame, ToDoCardPanel card) {
        this(parentFrame);
        this.cardDaModificare = card;
        setTitle("Modifica: " + card.getTitolo());

        txtTitolo.setText(card.getTitolo());
        txtData.setText(card.getScadenza());
        this.coloreSelezionato = card.getColore();
        btnColore.setBackground(coloreSelezionato);
    }

    // Costruttore Base
    private ToDoEditorDialog(JFrame parentFrame) {
        super(parentFrame, "Editor ToDo", true);
        setSize(400, 500);
        setLocationRelativeTo(parentFrame);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Titolo:"));
        txtTitolo = new JTextField();
        formPanel.add(txtTitolo);

        formPanel.add(new JLabel("Descrizione:"));
        txtDescrizione = new JTextArea(3, 20);
        formPanel.add(new JScrollPane(txtDescrizione));

        formPanel.add(new JLabel("Scadenza:"));
        txtData = new JTextField();
        formPanel.add(txtData);

        formPanel.add(new JLabel("Stato:"));
        chkCompletato = new JCheckBox("Completato");
        formPanel.add(chkCompletato);

        formPanel.add(new JLabel("Colore Sfondo:"));

        // BOTTONE CHE MOSTRA IL COLORE
        btnColore = new JButton(" ");
        btnColore.setOpaque(true);
        btnColore.setBorderPainted(false);
        btnColore.setBackground(coloreSelezionato);

        // Apre la finestra dei colori
        btnColore.addActionListener(e -> apriSelettorePastello());

        formPanel.add(btnColore);
        add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton btnSalva = new JButton("Salva");
        JButton btnAnnulla = new JButton("Annulla");

        btnSalva.addActionListener(e -> {
            if (cardDaModificare != null) {
                cardDaModificare.aggiornaDati(txtTitolo.getText(), txtData.getText(), coloreSelezionato, chkCompletato.isSelected());
            } else if (bachecaTarget != null) {
                bachecaTarget.aggiungiCard(txtTitolo.getText(), txtData.getText(), coloreSelezionato);
            }
            dispose();
        });

        btnAnnulla.addActionListener(e -> dispose());
        btnPanel.add(btnSalva);
        btnPanel.add(btnAnnulla);
        add(btnPanel, BorderLayout.SOUTH);
    }

    // METODO PER LA GRIGLIA DI COLORI
    private void apriSelettorePastello() {
        JDialog d = new JDialog(this, "Scegli Colore", true);
        d.setSize(350, 220); // Dimensioni perfette per la griglia 4x4
        d.setLocationRelativeTo(this);

        // Griglia 4 righe x 4 colonne = 16 colori
        JPanel p = new JPanel(new GridLayout(4, 4, 5, 5));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (Color c : coloriPastello) {
            JButton b = new JButton();
            b.setBackground(c);
            b.setOpaque(true);
            b.setBorderPainted(false);

            // Al click: imposta colore e chiude la finestra
            b.addActionListener(ev -> {
                coloreSelezionato = c;
                btnColore.setBackground(c);
                d.dispose();
            });
            p.add(b);
        }

        d.add(p);
        d.setVisible(true);
    }
}