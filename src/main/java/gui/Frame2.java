package gui;

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
    }
}