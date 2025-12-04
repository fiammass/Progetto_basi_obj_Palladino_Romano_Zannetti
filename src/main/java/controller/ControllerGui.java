package controller;

import gui.DashboardFrame;
import gui.LoginFrame;
import gui.BoardPanel; // Importa BoardPanel
import gui.ToDoEditorDialog; // Importa ToDoEditorDialog
import model.Utente;
import model.Bacheca;
import model.ToDo; // Richiede l'import
import java.awt.Color;
import java.time.LocalDate; // Richiede l'import
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JOptionPane;// Richiede l'import
import gui.ToDoCardPanel;

public class ControllerGui {
    //riferimento al gestore della logica (model)
    private ControllerLogica logica;
    private LoginFrame loginFrame;
    private DashboardFrame dashboardFrame;

    public ControllerGui(ControllerLogica controllerLogica) {
        this.logica = controllerLogica;
    }

    public void setLoginFrame(LoginFrame loginFrame) { // <--- Implementa questo metodo
        this.loginFrame = loginFrame;
        // Assicurati che il Controller si registri come Listener della View
        this.loginFrame.addListener(this);
    }
    public void setDashboardFrame(DashboardFrame dashboardFrame) {
        this.dashboardFrame = dashboardFrame;
        this.dashboardFrame.addListener(this);
    }
    public void handleLoginAttempt(String username, String password) {
        boolean loginValido = logica.checkLogin(username, password);
        if (loginValido) {
            loginFrame.dispose();

            if (dashboardFrame == null) {
                dashboardFrame = new DashboardFrame();
                this.setDashboardFrame(dashboardFrame);
            }
            Utente utente = logica.getUtenteCorrente();
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
        // Chiude la dashboard
        dashboardFrame.dispose();
        LoginFrame nuovaLogin = new LoginFrame();
        this.setLoginFrame(nuovaLogin);
        nuovaLogin.setVisible(true);
    }
    public void handleCercaToDo(String testo) {
        // 1. Logica di Business (cercare nel Model/Logica)
        ToDo risultatoTodo = logica.searchToDo(testo);

        // 2. Logica di Flusso (decidere cosa mostrare)
        if (risultatoTodo != null) {
            // Simulazione apertura SearchResultsDialog
            JOptionPane.showMessageDialog(dashboardFrame,
                    "Trovato ToDo: " + risultatoTodo.getTitolo() + " nella bacheca: " + risultatoTodo.getBacheca().getTitolo(),
                    "Ricerca Riuscita",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(dashboardFrame, "Nessun ToDo trovato con quel titolo.", "Ricerca Fallita", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    public void handleScadenzeOggi() {
        // 1. Logica di Business: ottiene i dati dal Model
        List<ToDo> scadenze = logica.getToDoEntroData(logica.getUtenteCorrente(), LocalDate.now());

        // 2. Logica di Flusso: mostra il risultato
        JOptionPane.showMessageDialog(dashboardFrame,
                "Trovate " + scadenze.size() + " attività in scadenza per oggi.",
                "Scadenze Oggi",
                JOptionPane.INFORMATION_MESSAGE);
    }
    // --- GESTIONE BOARDPANEL (Aggiunta nuovo ToDo) ---

    public void handleAggiungiToDo(BoardPanel targetPanel) {
        // 1. Logica: Trova il Model corrispondente al Panel
        Bacheca bachecaModel = dashboardFrame.getBachecaModelForPanel(targetPanel);

        if (bachecaModel != null) {
            // 2. Logica di Flusso: Apre il Dialog, passando Controller e Model
            // USIAMO IL COSTRUTTORE CORRETTO CHE ABBIAMO DEFINITO
            new ToDoEditorDialog(dashboardFrame, this, bachecaModel).setVisible(true);
        } else {
            // Gestione errore
            JOptionPane.showMessageDialog(dashboardFrame, "Errore: Bacheca non mappata al Model.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- GESTIONE EDITOR DIALOG (Salvataggio nuovo ToDo) ---

    public void handleSalvaNuovoToDo(String titolo, String dataScadenzaStr, String descrizione,
                                     Color colore, Bacheca bachecaTarget) {

        // 1. Conversione e Validazione (Parte della Logica)
        LocalDate scadenza = null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            scadenza = LocalDate.parse(dataScadenzaStr, formatter);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dashboardFrame, "Formato data scadenza errato (gg/MM/yyyy).", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2. Chiama la Logica per salvare nel DB
        ToDo nuovo = logica.createToDo(
                titolo,
                scadenza,
                null, // link
                descrizione,
                null, // immagine
                null, // immaginepath
                colore,
                logica.getUtenteCorrente(), // Autore
                bachecaTarget,
                false // Completato
        );

        // 3. Aggiornamento View
        if (nuovo != null) {
            dashboardFrame.aggiornaPanel(bachecaTarget.getIdBa(),
                    nuovo.getTitolo(), nuovo.getDatescadenza(), nuovo.getColor());
        }
    }
    public void handleModificaToDo(ToDoCardPanel card) {
        // 1. Logica: Trova l'oggetto Model associato alla Card
        // ASSUNZIONE: Devi avere un modo per ottenere l'oggetto ToDo Model dall'oggetto Card View.
        Object todoModel = null; // logica.getToDoByCard(card);

        // Eseguiamo la simulazione solo se abbiamo il riferimento alla Dashboard
        if (dashboardFrame == null) return;

        if (todoModel != null) {
            // 2. Logica di Flusso: Apre il dialogo, passando il Controller e il Model/Card.
            // Poiché il costruttore privato ha bisogno di essere chiamato da un costruttore PUBBLICO,
            // useremo il costruttore di modifica che avevamo definito:

            // **NOTA BENE:** Qui useremo un costruttore che CHIAMI il costruttore privato corretto.

            new ToDoEditorDialog(dashboardFrame, this, card, todoModel).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(dashboardFrame, "Errore: Dati ToDo non trovati.", "Modifica", JOptionPane.ERROR_MESSAGE);
        }
    }

}
    //riferimenti alle View principali
