package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class frame1  extends JFrame {

    private Bottone bottone;
    private Pannello textArea;
    private JTextField testo;

     public frame1() {

         super("Prova da mostrare a Gianna");

         setLayout(new BorderLayout());

         textArea = new Pannello();
         testo = new JTextField();
         bottone = new Bottone();

         add(testo, BorderLayout.PAGE_START);
         add(textArea, BorderLayout.CENTER);

         bottone.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {

                 Pannello.append()
                 testo.setText("");

             }
         });

         setLocationRelativeTo(null);

         setLocation(500,300);

         setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

         setSize(800, 500);

         setVisible(true);

    }
}
