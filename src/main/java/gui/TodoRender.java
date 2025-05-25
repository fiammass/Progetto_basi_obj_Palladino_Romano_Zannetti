package gui;

import javax.swing.*;
import java.awt.*;
import model.ToDo;
import model.Stato;

class TodoRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(
            JList<?> list, Object value, int index,
            boolean isSelected, boolean cellHasFocus) {

        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        ToDo todo = (ToDo) value;

        setText(todo.getTitolo() + " - " + todo.getStato().getDescrizione());

        // Colorazione in base allo stato
        switch (todo.getStato()) {
            case COMPLETATO:
                setBackground(new Color(200, 255, 200));
                break;
            case SCADUTO:
                setBackground(new Color(255, 200, 200));
                break;
            case IN_CORSO:
                setBackground(new Color(255, 255, 150));
                break;
            default:
                if (isSelected) {
                    setBackground(new Color(200, 200, 255));
                }
        }

        return this;
    }
}