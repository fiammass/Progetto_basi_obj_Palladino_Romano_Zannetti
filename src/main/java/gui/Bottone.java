package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Bottone extends JPanel {

    private JButton bottone1;
    private JButton bottone2;

    public Bottone(){

        bottone1 = new JButton("buongiorno");
        bottone2 = new JButton("Buonanotte");

        setLayout(new FlowLayout());

        add(bottone1 , FlowLayout.LEFT );
        add(bottone2 , FlowLayout.LEFT ) ;





    }


}
