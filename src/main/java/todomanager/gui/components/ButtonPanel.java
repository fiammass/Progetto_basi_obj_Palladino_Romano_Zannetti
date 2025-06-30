package com.todomanager.gui.components;

import javax.swing.*;
import java.awt.*;

public class ButtonPanel extends JPanel {
    public ButtonPanel() {
        setLayout(new FlowLayout(FlowLayout.RIGHT));
        add(new JButton("Salva"));
        add(new JButton("Annulla"));
    }
}