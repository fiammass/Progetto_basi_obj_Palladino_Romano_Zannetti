package controller;

import gui.*;
import model.Utente;
import model.Bacheca;
import model.ToDo;
import java.awt.Color;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;


/**
 * Classe del controller per collegare la gui al controllerLogica
 */
public class ControllerGui {

    private ControllerLogica logica;
    private LoginFrame loginFrame;
    private DashboardFrame dashboardFrame;


    private Map<ToDoCardPanel, ToDo> mappaCardModel = new HashMap<>();

    public ControllerGui(ControllerLogica controllerLogica) {
        this.logica = controllerLogica;
    }


    /**
     * Da chiamare quando la View crea una Card grafica per un ToDo.
     * Permette al controller di recuperare l'oggetto ToDo quando si clicca sulla Card.
     */
    public void registraAssociazione(ToDoCardPanel card, ToDo model) {
        mappaCardModel.put(card, model);
    }


    /**
     * Metodo per "inizializzare" la finestra del login
     * @param loginFrame
     */
    public void setLoginFrame(LoginFrame loginFrame) {
        this.loginFrame = loginFrame;
        // this.loginFrame.addListener(this); // Decommenta se usi listener custom
    }

    /**
     * Metodo per "inizializzare" la finestra della dashboard
     * @param dashboardFrame
     */

    public void setDashboardFrame(DashboardFrame dashboardFrame) {
        this.dashboardFrame = dashboardFrame;
        this.dashboardFrame.addListener(this);
    }

    // --- LOGIN & LOGOUT ---

    /**
     * Metodo per verificare login e logout dalla bacheca
     * @param username
     * @param password
     */
    public void handleLoginAttempt(String username, String password) {
        boolean loginValido = logica.checkLogin(username, password);
        if (loginValido) {
            loginFrame.dispose();
            if (dashboardFrame == null) {
                dashboardFrame = new DashboardFrame();
                this.setDashboardFrame(dashboardFrame);
            }

            Utente utente = logica.getUtenteCorrente();
            // Passiamo le bacheche alla View per visualizzarle
            dashboardFrame.displayBacheche(
                    utente.getBacheca1(),
                    utente.getBacheca2(),
                    utente.getBacheca3()
            );
            dashboardFrame.setVisible(true);
        } else {
            loginFrame.showErrorMessage("Credenziali non valide.");
        }
    }

    public void handleLogout() {
        if(dashboardFrame != null) dashboardFrame.dispose();
        // Ricrea il login pulito
        ControllerLogica nuovaLogica = new ControllerLogica();
        ControllerGui nuovoController = new ControllerGui(nuovaLogica);
        LoginFrame nuovaLogin = new LoginFrame(nuovoController);
        nuovoController.setLoginFrame(nuovaLogin);
        nuovaLogin.setVisible(true);
    }

    // --- GESTIONE TODO ---

    /**
     * Metodo per cercare un ToDo
     * @param testo
     */

    public void handleCercaToDo(String testo) {
        ToDo risultato = logica.searchToDo(testo);
        if (risultato != null) {
            JOptionPane.showMessageDialog(dashboardFrame,
                    "Trovato: " + risultato.getTitolo() + "\nBacheca: " + risultato.getBacheca().getTitolo(),
                    "Ricerca Riuscita", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(dashboardFrame, "Nessun ToDo trovato.", "Ricerca Fallita", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Metodo per verificare quale Todo scade
     */

    public void handleScadenzeOggi() {
        List<ToDo> scadenze = logica.getToDoEntroData(logica.getUtenteCorrente(), LocalDate.now());
        JOptionPane.showMessageDialog(dashboardFrame,
                "Hai " + scadenze.size() + " attività in scadenza oggi.",
                "Scadenze", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Metodo per aggiornare i ToDo
     * @param targetPanel
     */

    public void handleAggiungiToDo(BoardPanel targetPanel) {
        // Recupera il model della bacheca dal panel (La Dashboard deve saperlo fare)
        Bacheca bachecaModel = dashboardFrame.getBachecaModelForPanel(targetPanel);

        if (bachecaModel != null) {
            new ToDoEditorDialog(dashboardFrame, this, bachecaModel).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(dashboardFrame, "Errore: Bacheca non trovata.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Metodo per salvare un nuovo ToDo nella bachehca
     * @param titolo
     * @param dataScadenzaStr
     * @param descrizione
     * @param colore
     * @param bachecaTarget
     */

    public void handleSalvaNuovoToDo(String titolo, String dataScadenzaStr, String descrizione,
                                     Color colore, Bacheca bachecaTarget) {
        LocalDate scadenza = null;
        try {
            if(dataScadenzaStr != null && !dataScadenzaStr.isEmpty())
                scadenza = LocalDate.parse(dataScadenzaStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dashboardFrame, "Data errata (usa gg/MM/yyyy).", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ToDo nuovo = logica.createToDo(titolo, scadenza, null, descrizione, null, null, colore,
                logica.getUtenteCorrente(), bachecaTarget, false);

        if (nuovo != null) {
            // Aggiorna la grafica
            dashboardFrame.aggiornaPanel(bachecaTarget.getIdBa(), nuovo.getTitolo(), nuovo.getDatescadenza(), nuovo.getColor());
        }
    }

    /**
     * Metodo per modificare un Todo gia presente in bacheca
     * @param cardPanel
     */

    public void handleModificaToDo(ToDoCardPanel cardPanel) {
        // 1. Recupera il Model dalla mappa che abbiamo creato
        ToDo todoModel = mappaCardModel.get(cardPanel);

        if (todoModel != null) {
            // 2. Apre l'editor popolato coi dati esistenti
            new ToDoEditorDialog(dashboardFrame, this, cardPanel, todoModel).setVisible(true);
        } else {
            // Fallback se la mappa non è sincronizzata
            JOptionPane.showMessageDialog(dashboardFrame,
                    "Errore: Impossibile recuperare i dati del ToDo. (Hai chiamato registraAssociazione?)",
                    "Errore Tecnico", JOptionPane.ERROR_MESSAGE);
        }
    }
}