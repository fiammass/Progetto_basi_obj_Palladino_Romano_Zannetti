package gui;

import javax.swing.*;
import java.awt.*;

public class Pannello extends JPanel {

    JTextArea textArea;

    public Pannello(){

        textArea = new JTextArea();

        setLayout(new BorderLayout());

        add(textArea, BorderLayout.CENTER);
    }

    public voi appenditesto(String testo){

        textArea.append(testo);
    }

}
