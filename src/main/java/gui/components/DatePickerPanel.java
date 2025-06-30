package gui.components;

import javax.swing.*;
import java.time.LocalDate;

public class DatePickerPanel extends JPanel {
    private final JSpinner spinner;

    public DatePickerPanel() {
        SpinnerDateModel model = new SpinnerDateModel();
        spinner = new JSpinner(model);
        spinner.setEditor(new JSpinner.DateEditor(spinner, "dd/MM/yyyy"));

        add(new JLabel("Data scadenza:"));
        add(spinner);
    }

    public LocalDate getSelectedDate() {
        return ((java.util.Date) spinner.getValue()).toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate();
    }
}