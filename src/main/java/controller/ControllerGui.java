package controller;

import gui.*;
import model.Utente;
import model.Bacheca;
import model.CheckList;
import model.ToDo;
import java.awt.Color;
import java.awt.Container;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Controller GUI: Gestisce l'interazione tra le finestre grafiche e la logica di business.
 */
public class ControllerGui {

    private ControllerLogica logica;
    private LoginFrame loginFrame;
    private DashboardFrame dashboardFrame;

    public ControllerGui(ControllerLogica controllerLogica) {
        this.logica = controllerLogica;
    }

    // --- SETUP FINESTRE ---
    public void setLoginFrame(LoginFrame loginFrame) { this.loginFrame = loginFrame; }

    public void setDashboardFrame(DashboardFrame dashboardFrame) {
        this.dashboardFrame = dashboardFrame;
        this.dashboardFrame.addListener(this);
    }

    // --- LOGIN & LOGOUT ---
    public void handleLoginAttempt(String username, String password) {
        if (logica.checkLogin(username, password)) {
            loginFrame.dispose();
            if (dashboardFrame == null) {
                dashboardFrame = new DashboardFrame();
                setDashboardFrame(dashboardFrame);
            }
            Utente u = logica.getUtenteCorrente();
            dashboardFrame.displayBacheche(u.getBacheca1(), u.getBacheca2(), u.getBacheca3());
            dashboardFrame.setVisible(true);
        } else {
            loginFrame.showErrorMessage("Credenziali non valide.");
        }
    }

    public void handleLogout() {
        if (dashboardFrame != null) dashboardFrame.dispose();
        ControllerLogica nuovaLogica = new ControllerLogica();
        ControllerGui nuovoController = new ControllerGui(nuovaLogica);
        LoginFrame nuovaLogin = new LoginFrame(nuovoController);
        nuovoController.setLoginFrame(nuovaLogin);
        nuovaLogin.setVisible(true);
    }

    // --- GESTIONE TODO ---

    public void handleAggiungiToDo(BoardPanel targetPanel) {
        Bacheca bachecaModel = dashboardFrame.getBachecaModelForPanel(targetPanel);
        if (bachecaModel != null) {
            new ToDoEditorDialog(dashboardFrame, this, bachecaModel).setVisible(true);
        }
    }

    public void handleSalvaNuovoToDo(String titolo, String dataScadenzaStr, String descrizione,
                                     Color colore, Bacheca bachecaTarget) {
        LocalDate scadenza = null;
        try {
            if (dataScadenzaStr != null && !dataScadenzaStr.isEmpty())
                scadenza = LocalDate.parse(dataScadenzaStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dashboardFrame, "Data errata (usa gg/MM/yyyy).");
            return;
        }

        ToDo nuovo = logica.createToDo(titolo, scadenza, null, descrizione, null, null, colore,
                logica.getUtenteCorrente(), bachecaTarget, false);

        if (nuovo != null) {
            dashboardFrame.aggiornaPanel(bachecaTarget.getIdBa(), nuovo);
        }
    }

    public void handleModificaToDo(ToDoCardPanel cardPanel) {
        ToDo todoModel = cardPanel.getToDo();
        if (todoModel != null) {
            new ToDoEditorDialog(dashboardFrame, this, cardPanel, todoModel).setVisible(true);
        }
    }

    public void handleSalvaModifica(ToDo todo, ToDoCardPanel card, String titolo, String descrizione,
                                    LocalDate scadenza, Color colore, boolean completato) {
        todo.setTitolo(titolo);
        todo.setDescrizione(descrizione);
        todo.setDatescadenza(scadenza);
        todo.setCompletato(completato);
        todo.setColor(colore);

        logica.modificaToDo(todo, titolo, descrizione, todo.getUrl(), scadenza, colore, null, null);

        if (card != null) card.aggiornaGrafica();
    }

    public void handleEliminaToDo(ToDo todo, ToDoCardPanel card) {
        logica.eliminaToDo(todo);
        if (card != null) {
            Container parent = card.getParent();
            if (parent != null) {
                parent.remove(card);
                parent.revalidate();
                parent.repaint();
            }
        }
    }

    // --- GESTIONE CHECKLIST (NUOVO) ---

    public void handleAggiungiCheckList(ToDo todo, String nomeAttivita) {
        if (todo == null || todo.getIdToDo() == null) {
            JOptionPane.showMessageDialog(dashboardFrame, "Devi salvare il ToDo prima di aggiungere la checklist!");
            return;
        }
        CheckList nuova = new CheckList(nomeAttivita);
        // Salva nel DB e aggiorna il model
        logica.salvaCheckListAttivita(nuova, todo);
    }

    public void handleUpdateStatoCheckList(CheckList item, boolean completato, ToDo todo) {
        logica.updateStatoCheckList(item, completato, todo);
    }

    public void handleEliminaCheckList(CheckList item, ToDo todo) {
        logica.eliminaCheckListAttivita(item, todo);
    }

    // --- UTILITY ---

    public void handleCercaToDo(String testo) {
        ToDo risultato = logica.searchToDo(testo);
        if (risultato != null) {
            JOptionPane.showMessageDialog(dashboardFrame, "Trovato: " + risultato.getTitolo() + " in " + risultato.getBacheca().getTitolo());
        } else {
            JOptionPane.showMessageDialog(dashboardFrame, "Nessun ToDo trovato.");
        }
    }

    public void handleScadenzeOggi() {
        List<ToDo> scadenze = logica.getToDoEntroData(logica.getUtenteCorrente(), LocalDate.now());
        JOptionPane.showMessageDialog(dashboardFrame, "Attività in scadenza oggi: " + scadenze.size());
    }

    public void registraAssociazione(ToDoCardPanel card, ToDo model) {}
}