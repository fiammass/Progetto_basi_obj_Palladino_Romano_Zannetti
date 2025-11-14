package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Frame1 extends JFrame{

    private JPanel loginpanel;
    private JButton button1;
    private JLabel passwtext;
    private JPasswordField passwordField1;
    private JLabel logintext;
    private JTextField textField1;

    public Frame1() {

        setContentPane(loginpanel);

        getRootPane().setDefaultButton(button1);

        setTitle("Benvenutx!!");

        setLocationRelativeTo(null);

        setLocation(500, 300);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setSize(800, 500);

        setVisible(true);

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String passwordInserita = new String(passwordField1.getPassword());
                String utenteInserito = new String(textField1.getText());

                if (passwordInserita.equals("ciao") && utenteInserito.equals("admin")) {
                    dispose();
                    Frame2 frame2 = new Frame2();

                } else {
                    JOptionPane.showMessageDialog(Frame1.this, "Password  o nome utente errato!", "Errore", JOptionPane.ERROR_MESSAGE);
                }
                passwordInserita = null;
            }
        });
    }
}