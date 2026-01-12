package gui;

import controller.ControllerGui;
import model.Bacheca;
import model.ToDo;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ToDoEditorDialog extends JDialog {

    private JTextField txtTitolo;
    private JTextArea txtDescrizione;
    private JTextField txtData;
    private JCheckBox chkCompletato;
    private JButton btnColore;
    private JButton btnSalva;
    private JButton btnElimina; // NUOVO BOTTONE

    private Color coloreSelezionato = new Color(255, 255, 153);

    private Bacheca bachecaTargetModel;
    private ToDo todoInModifica;
    private ToDoCardPanel cardGrafica;
    private ControllerGui controller;

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final Color[] coloriPastello = {
            new Color(255, 255, 255), new Color(255, 250, 205), new Color(255, 255, 153), new Color(255, 229, 204),
            new Color(255, 218, 185), new Color(255, 182, 193), new Color(255, 204, 255), new Color(221, 160, 221),
            new Color(230, 230, 250), new Color(204, 229, 255), new Color(173, 216, 230), new Color(204, 255, 255),
            new Color(204, 255, 204), new Color(152, 251, 152), new Color(240, 240, 240), new Color(211, 211, 211)
    };

    public ToDoEditorDialog(JFrame parentFrame, ControllerGui controller, Bacheca bachecaModel) {
        this(parentFrame, controller);
        this.bachecaTargetModel = bachecaModel;
        setTitle("Nuovo ToDo in " + bachecaModel.getTitolo());
        chkCompletato.setVisible(false);
        btnElimina.setVisible(false); // Non si può eliminare un todo che stiamo ancora creando
    }

    public ToDoEditorDialog(JFrame parentFrame, ControllerGui controller, ToDoCardPanel card, ToDo todoModel) {
        this(parentFrame, controller);
        this.cardGrafica = card;
        this.todoInModifica = todoModel;
        setTitle("Modifica: " + todoModel.getTitolo());

        txtTitolo.setText(todoModel.getTitolo());
        txtDescrizione.setText(todoModel.getDescrizione());

        if (todoModel.getDatescadenza() != null) {
            txtData.setText(todoModel.getDatescadenza().format(dtf));
        }

        if (todoModel.getColor() != null) {
            this.coloreSelezionato = todoModel.getColor();
            btnColore.setBackground(coloreSelezionato);
        }

        chkCompletato.setSelected(Boolean.TRUE.equals(todoModel.getCompletato()));

        // Se stiamo modificando, mostriamo il tasto elimina
        btnElimina.setVisible(true);
    }

    private ToDoEditorDialog(JFrame parentFrame, ControllerGui controller) {
        super(parentFrame, "Editor ToDo", true);
        this.controller = controller;
        setSize(400, 550);
        setLocationRelativeTo(parentFrame);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1.0;

        addLabelAndComponent(formPanel, gbc, 0, "Titolo:", txtTitolo = new JTextField());
        addLabelAndComponent(formPanel, gbc, 1, "Descrizione:", new JScrollPane(txtDescrizione = new JTextArea(4, 20)));
        addLabelAndComponent(formPanel, gbc, 2, "Scadenza (gg/mm/aaaa):", txtData = new JTextField());

        chkCompletato = new JCheckBox("Completato");
        gbc.gridx = 1; gbc.gridy = 3;
        formPanel.add(chkCompletato, gbc);

        btnColore = new JButton(" ");
        btnColore.setOpaque(true);
        btnColore.setBorderPainted(false);
        btnColore.setBackground(coloreSelezionato);
        btnColore.addActionListener(e -> apriSelettorePastello());
        addLabelAndComponent(formPanel, gbc, 4, "Colore Sfondo:", btnColore);

        add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();

        // BOTTONE ELIMINA
        btnElimina = new JButton("Elimina");
        btnElimina.setBackground(new Color(255, 102, 102)); // Rosso chiaro
        btnElimina.setForeground(Color.WHITE);
        btnElimina.addActionListener(e -> gestisciEliminazione());

        btnSalva = new JButton("Salva");
        JButton btnAnnulla = new JButton("Annulla");

        btnSalva.addActionListener(e -> gestisciSalvataggio());
        btnAnnulla.addActionListener(e -> dispose());

        btnPanel.add(btnElimina); // Aggiungiamo il tasto elimina a sinistra
        btnPanel.add(Box.createHorizontalStrut(20)); // Spazio
        btnPanel.add(btnSalva);
        btnPanel.add(btnAnnulla);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void gestisciEliminazione() {
        int conferma = JOptionPane.showConfirmDialog(this,
                "Sei sicuro di voler eliminare questo ToDo?\nL'operazione è irreversibile.",
                "Conferma Eliminazione",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (conferma == JOptionPane.YES_OPTION) {
            controller.handleEliminaToDo(todoInModifica, cardGrafica);
            dispose(); // Chiude la finestra
        }
    }

    private void gestisciSalvataggio() {
        try {
            String titolo = txtTitolo.getText().trim();
            if (titolo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Il titolo è obbligatorio.");
                return;
            }

            String dataStr = txtData.getText().trim();
            String descrizione = txtDescrizione.getText();
            LocalDate scadenza = null;

            if (!dataStr.isEmpty()) {
                try {
                    scadenza = LocalDate.parse(dataStr, dtf);
                    if (scadenza.isBefore(LocalDate.now()) && !chkCompletato.isSelected()) {
                        // Data passata, ma permettiamo se l'utente vuole
                    }
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(this, "Data non valida. Usa gg/mm/aaaa");
                    return;
                }
            }

            if (todoInModifica != null) {
                controller.handleSalvaModifica(todoInModifica, cardGrafica, titolo, descrizione, scadenza, coloreSelezionato, chkCompletato.isSelected());
            } else if (bachecaTargetModel != null) {
                controller.handleSalvaNuovoToDo(titolo, dataStr, descrizione, coloreSelezionato, bachecaTargetModel);
            }

            dispose();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Errore salvataggio: " + e.getMessage());
        }
    }

    private void addLabelAndComponent(JPanel p, GridBagConstraints gbc, int row, String text, Component comp) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        p.add(new JLabel(text), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        p.add(comp, gbc);
    }

    private void apriSelettorePastello() {
        JDialog d = new JDialog(this, "Scegli Colore", true);
        d.setSize(350, 220);
        d.setLocationRelativeTo(this);
        JPanel p = new JPanel(new GridLayout(4, 4, 5, 5));

        for (Color c : coloriPastello) {
            JButton b = new JButton();
            b.setBackground(c);
            b.setOpaque(true);
            b.setBorderPainted(false);
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