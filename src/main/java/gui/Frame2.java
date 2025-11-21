package gui;

import model.Bacheca;
import controller.ControllerLogica;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Frame2 extends JFrame{

    private JPanel frame2;
    private JButton universitaButton;
    private JButton tempoLiberoButton;
    private JButton Lavorobutton;
    private JButton backButton;

    public Frame2() {

        setContentPane(frame2);

        setTitle("Benvenutx!!");

        setLocationRelativeTo(null);

        setLocation(500, 300);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setSize(800, 500);

        setVisible(true);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                dispose();

                Frame1 frame1 = new Frame1();
                frame1.setVisible(true);
            }
        });
       /* Lavorobutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 1. Recuperiamo la bacheca "Tempo Libero" dal Controller
                // (Nel tuo controller: 1=Uni, 2=Lavoro, 3=Tempo Libero)
                Bacheca bachecaTempoLibero = c(2);

                if (bachecaTempoLibero != null) {
                    // 2. Creiamo la nuova vista dedicata a questa bacheca
                    // Nota: "BachecaView" è il nome ipotetico della tua classe che mostra una singola bacheca.
                    // Sostituiscilo con il nome reale della tua classe (es. SingleBachecaView, DetailView, etc.)
                    // Devi passare: controller, l'utente e la bacheca specifica.
                    BachecaView viewSingola = new BachecaView(new Cont, utentecorrente, bachecaTempoLibero);

                    // 3. Mostriamo la finestra e chiudiamo quella attuale (opzionale)
                    viewSingola.setVisible(true);

                    // Togli il commento sotto se vuoi che la Home si chiuda quando apri la bacheca
                    // dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Errore: Bacheca non trovata.");
                }
            }
        }); */
    }
}