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

/**
 * Classe Controller che gestisce l'interazione tra l'interfaccia grafica (View)
 * e la logica di business (Model/ControllerLogica).
 * Si occupa di ricevere gli input dall'utente tramite le finestre, invocare le operazioni
 * necessarie sul database tramite {@link ControllerLogica} e aggiornare la vista di conseguenza.
 */
public class ControllerGui {

    private ControllerLogica logica;
    private LoginFrame loginFrame;
    private DashboardFrame dashboardFrame;

    /**
     * Costruttore della classe ControllerGui.
     * Inizializza il collegamento con la logica di business.
     *
     * @param controllerLogica L'istanza del controller logico che gestisce i dati.
     */
    public ControllerGui(ControllerLogica controllerLogica) {
        this.logica = controllerLogica;
    }

    /**
     * Imposta il riferimento alla finestra di Login.
     *
     * @param loginFrame La finestra di login.
     */
    public void setLoginFrame(LoginFrame loginFrame) { this.loginFrame = loginFrame; }

    /**
     * Imposta il riferimento alla finestra principale (Dashboard) e aggiunge questo controller come listener.
     *
     * @param dashboardFrame La finestra principale dell'applicazione.
     */
    public void setDashboardFrame(DashboardFrame dashboardFrame) {
        this.dashboardFrame = dashboardFrame;
        this.dashboardFrame.addListener(this);
    }

    /**
     * Gestisce il tentativo di accesso dell'utente.
     * Verifica le credenziali tramite la logica. Se corrette, chiude il login,
     * inizializza la dashboard e carica le bacheche dell'utente.
     * Altrimenti mostra un messaggio di errore.
     *
     * @param username Il nome utente inserito.
     * @param password La password inserita.
     */
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

    /**
     * Gestisce il logout dell'utente.
     * Chiude la dashboard corrente e riapre una nuova finestra di login,
     * reinizializzando i controller per garantire una sessione pulita.
     */
    public void handleLogout() {
        if (dashboardFrame != null) dashboardFrame.dispose();
        ControllerLogica nuovaLogica = new ControllerLogica();
        ControllerGui nuovoController = new ControllerGui(nuovaLogica);
        LoginFrame nuovaLogin = new LoginFrame(nuovoController);
        nuovoController.setLoginFrame(nuovaLogin);
        nuovaLogin.setVisible(true);
    }

    /**
     * Apre la finestra di dialogo per creare un nuovo ToDo all'interno di una specifica bacheca.
     *
     * @param targetPanel Il pannello grafico della bacheca in cui aggiungere il ToDo.
     */
    public void handleAggiungiToDo(BoardPanel targetPanel) {
        Bacheca bachecaModel = dashboardFrame.getBachecaModelForPanel(targetPanel);
        if (bachecaModel != null) {
            new ToDoEditorDialog(dashboardFrame, this, bachecaModel).setVisible(true);
        }
    }

    /**
     * Gestisce il salvataggio di un nuovo ToDo nel database e aggiorna l'interfaccia.
     * Effettua il parsing della data e la validazione dei dati.
     *
     * @param titolo           Il titolo del ToDo.
     * @param dataScadenzaStr  La data di scadenza in formato stringa (dd/MM/yyyy).
     * @param descrizione      La descrizione del ToDo.
     * @param colore           Il colore di sfondo scelto.
     * @param bachecaTarget    La bacheca di destinazione.
     */
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

    /**
     * Apre la finestra di dialogo per modificare un ToDo esistente.
     *
     * @param cardPanel Il pannello grafico (card) che rappresenta il ToDo da modificare.
     */
    public void handleModificaToDo(ToDoCardPanel cardPanel) {
        ToDo todoModel = cardPanel.getToDo();
        if (todoModel != null) {
            new ToDoEditorDialog(dashboardFrame, this, cardPanel, todoModel).setVisible(true);
        }
    }

    /**
     * Salva le modifiche apportate a un ToDo esistente.
     * Aggiorna il modello locale, invoca l'aggiornamento sul database e ridisegna la card grafica.
     *
     * @param todo        L'oggetto ToDo da modificare.
     * @param card        Il riferimento grafico al ToDo (per aggiornare il colore/testo).
     * @param titolo      Il nuovo titolo.
     * @param descrizione La nuova descrizione.
     * @param scadenza    La nuova data di scadenza.
     * @param colore      Il nuovo colore.
     * @param completato  Il nuovo stato di completamento.
     */
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

    /**
     * Gestisce l'eliminazione di un ToDo.
     * Rimuove il ToDo dal database e aggiorna l'interfaccia rimuovendo la card.
     *
     * @param todo L'oggetto ToDo da eliminare.
     * @param card Il componente grafico da rimuovere dalla vista.
     */
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

    /**
     * Aggiunge una nuova attività alla checklist di un ToDo.
     * Controlla che il ToDo sia già salvato (abbia un ID) prima di procedere.
     *
     * @param todo         Il ToDo a cui aggiungere l'attività.
     * @param nomeAttivita Il testo dell'attività da aggiungere.
     */
    public void handleAggiungiCheckList(ToDo todo, String nomeAttivita) {
        if (todo == null || todo.getIdToDo() == null) {
            JOptionPane.showMessageDialog(dashboardFrame, "Devi salvare il ToDo prima di aggiungere la checklist!");
            return;
        }
        CheckList nuova = new CheckList(nomeAttivita);
        logica.salvaCheckListAttivita(nuova, todo);
    }

    /**
     * Aggiorna lo stato (completato/non completato) di una voce della checklist.
     *
     * @param item       L'oggetto CheckList da aggiornare.
     * @param completato Il nuovo stato booleano.
     * @param todo       Il ToDo padre (per verificare l'autocompletamento).
     */
    public void handleUpdateStatoCheckList(CheckList item, boolean completato, ToDo todo) {
        logica.updateStatoCheckList(item, completato, todo);
    }

    /**
     * Elimina una voce dalla checklist di un ToDo.
     *
     * @param item L'oggetto CheckList da eliminare.
     * @param todo Il ToDo padre.
     */
    public void handleEliminaCheckList(CheckList item, ToDo todo) {
        logica.eliminaCheckListAttivita(item, todo);
    }

    /**
     * Gestisce la condivisione di un ToDo con un altro utente.
     * Verifica che l'utente corrente sia il proprietario del ToDo prima di consentire l'operazione.
     *
     * @param todo                 Il ToDo da condividere.
     * @param usernameDestinatario Lo username dell'utente con cui condividere.
     */
    public void handleAggiungiCondivisione(ToDo todo, String usernameDestinatario) {
        if (!logica.getUtenteCorrente().getlogin().equals(todo.getAutore().getlogin())) {
            JOptionPane.showMessageDialog(dashboardFrame, "Solo il proprietario (" + todo.getAutore().getlogin() + ") può condividere questo ToDo!");
            return;
        }
        boolean esito = logica.condividiToDo(todo, usernameDestinatario);
        if (esito) {
            JOptionPane.showMessageDialog(dashboardFrame, "ToDo condiviso con: " + usernameDestinatario);
        } else {
            JOptionPane.showMessageDialog(dashboardFrame, "Errore: Utente non trovato.");
        }
    }

    /**
     * Rimuove la condivisione di un ToDo con uno specifico utente.
     * Solo il proprietario può eseguire questa azione.
     *
     * @param todo     Il ToDo in questione.
     * @param username Lo username dell'utente da rimuovere dalla condivisione.
     */
    public void handleRimuoviCondivisione(ToDo todo, String username) {
        if (!logica.getUtenteCorrente().getlogin().equals(todo.getAutore().getlogin())) {
            JOptionPane.showMessageDialog(dashboardFrame, "Solo il proprietario può rimuovere condivisioni!");
            return;
        }
        logica.rimuoviCondivisione(todo, username);
    }

    /**
     * Restituisce lo username dell'utente attualmente loggato.
     *
     * @return La stringa contenente lo username.
     */
    public String getUsernameCorrente() {
        return logica.getUtenteCorrente().getlogin();
    }

    /**
     * Esegue una ricerca dei ToDo per titolo.
     * Mostra un messaggio con il risultato se trovato, o un avviso se non trovato.
     *
     * @param testo Il testo da cercare nel titolo.
     */
    public void handleCercaToDo(String testo) {
        ToDo risultato = logica.searchToDo(testo);
        if (risultato != null) {
            JOptionPane.showMessageDialog(dashboardFrame, "Trovato: " + risultato.getTitolo() + " in " + risultato.getBacheca().getTitolo());
        } else {
            JOptionPane.showMessageDialog(dashboardFrame, "Nessun ToDo trovato.");
        }
    }

    /**
     * Conta e visualizza il numero di attività in scadenza nella data odierna.
     */
    public void handleScadenzeOggi() {
        List<ToDo> scadenze = logica.getToDoEntroData(logica.getUtenteCorrente(), LocalDate.now());
        JOptionPane.showMessageDialog(dashboardFrame, "Attività in scadenza oggi: " + scadenze.size());
    }

    /**
     * Metodo di utilità per registrare associazioni grafiche (placeholder).
     * Attualmente non implementato.
     *
     * @param card  Il pannello grafico.
     * @param model Il modello ToDo.
     */
    public void registraAssociazione(ToDoCardPanel card, ToDo model) {}
}