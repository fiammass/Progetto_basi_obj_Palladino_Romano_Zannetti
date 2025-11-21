package controller;

import implementazioni.postgres.dao.BachecaImplementazione;
import implementazioni.postgres.dao.CheckListImplementazione;
import implementazioni.postgres.dao.CondivisioneImplementazionePostgreDAO;
import implementazioni.postgres.dao.ToDoImplementazione;
import implementazioni.postgres.dao.UtenteImplementazione;
import dao.BachecaDao;
import dao.ChecklistDao;
import dao.CondivisioneDAO;
import dao.ToDoDao;
import dao.UtenteDao;
import model.*;

import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Rappresenta il controller, che si occupa della comunicazione tra Model e Database.
 * Gestisce Utenti, ToDo, Bacheche, Condivisioni e Checklist.
 */
public class ControllerLogica {

    final List<Utente> utenti;
    private Utente utenteCorrente;

    // DAO Interfaces
    private final UtenteDao utenteDAO;
    private final ToDoDao todoDAO; // Usa l'interfaccia ToDoDao fornita
    private final BachecaDao bachecaDAO;
    private final CondivisioneDAO condivisioneDAO;
    private final ChecklistDao checklistDao; // Nuovo DAO per le Checklist

    /**
     * Costruttore della classe controller.
     * Inizializza tutte le implementazioni dei DAO.
     *
     * @param utenti lista utenti del model (se mantenuta in memoria)
     */
    public ControllerLogica(List<Utente> utenti) {
        this.utenteDAO = new UtenteImplementazione();
        this.todoDAO = new ToDoImplementazione();
        this.bachecaDAO = new BachecaImplementazione();
        this.condivisioneDAO = new CondivisioneImplementazionePostgreDAO();
        this.checklistDao = new CheckListImplementazione();

        this.utenti = utenti;
    }

    /**
     * Controlla le credenziali inserite dall'utente per verificare il login.
     * In caso di successo, carica i dati completi (ToDo, Bacheche, Checklist).
     *
     * @param username l'username
     * @param password la password
     * @return true se il login è valido, false altrimenti.
     */
    public boolean checkLogin(String username, String password) {
        if (utenteDAO.VerificaLogin(username, password)) {
            utenteCorrente = utenteDAO.getUtentebyUsername(username);

            List<Bacheca> bacheche = bachecaDAO.getBachecaByUtente(username);

            // Assumiamo che ci siano sempre le 3 bacheche standard
            if (bacheche != null && bacheche.size() >= 3) {
                utenteCorrente.setBacheca1(bacheche.get(0));
                utenteCorrente.setBacheca2(bacheche.get(1));
                utenteCorrente.setBacheca3(bacheche.get(2));

                caricaDati(utenteCorrente); // Carica ToDo e Checklist
                return true;
            }
        }
        return false;
    }

    /**
     * Aggiunge un nuovo utente al sistema e collega le bacheche predefinite.
     *
     * @param username l'username
     * @param password la password
     * @return l'utente aggiunto
     */
    public Utente aggiungiUtente(String username, String password) {
        Utente utente = new Utente(username, password);
        utenteDAO.salvautente(utente);

        List<Bacheca> bachecheDb = bachecaDAO.getBachecaByUtente(username);
        for (Bacheca bDb : bachecheDb) {
            switch (bDb.getTitolo()) {
                case "Università":
                    utente.getBacheca1().setIdBa(bDb.getIdBa());
                    break;
                case "Lavoro":
                    utente.getBacheca2().setIdBa(bDb.getIdBa());
                    break;
                case "Tempo Libero":
                    utente.getBacheca3().setIdBa(bDb.getIdBa());
                    break;
                default:
                    break;
            }
        }
        return utente;
    }

    /**
     * Aggiorna titolo e descrizione di una bacheca.
     */
    public void aggiornaBacheca(Bacheca bacheca, String nuovoNome, String nuovaDesc) {
        bacheca.setTitolo(nuovoNome);
        bacheca.setDescrizione(nuovaDesc);
        bachecaDAO.updateBacheca(bacheca, bacheca.getIdBa());
    }

    /**
     * Restituisce la bacheca dell'utente corrente in base all'indice (1, 2, 3).
     */
    public Bacheca getBachecaCorrente(int numeroBacheca) {
        if (utenteCorrente == null) return null;
        return switch (numeroBacheca) {
            case 1 -> utenteCorrente.getBacheca1();
            case 2 -> utenteCorrente.getBacheca2();
            case 3 -> utenteCorrente.getBacheca3();
            default -> null;
        };
    }

    /**
     * Cerca un ToDo per titolo tra tutte le bacheche dell'utente corrente.
     */
    public ToDo searchToDo(String titolo) {
        List<Bacheca> bacheche = Arrays.asList(utenteCorrente.getBacheca1(), utenteCorrente.getBacheca2(), utenteCorrente.getBacheca3());
        for (Bacheca bacheca : bacheche) {
            for (ToDo todo : bacheca.getTodos()) {
                if (todo.getTitolo().equalsIgnoreCase(titolo)) {
                    return todo;
                }
            }
        }
        return null;
    }

    /**
     * Recupera un utente dal DB tramite username.
     */
    public Utente getUtenteByUsername(String username) {
        return utenteDAO.getUtentebyUsername(username);
    }

    /**
     * Crea un nuovo ToDo, lo salva nel DB e lo aggiunge alla bacheca in memoria.
     */
    public ToDo createToDo(String titoloTodo, LocalDate scadenza, String link, String descrizioneTodo,
                           Image immagine, String immaginepath, Color colore,
                           Utente autore, Bacheca bacheca, Boolean completato) {

        if (titoloTodo == null || titoloTodo.trim().isEmpty()) {
            // In un controller puro, qui si potrebbe lanciare un'eccezione o ritornare null
            return null;
        }

        ToDo nuovo = new ToDo(titoloTodo, scadenza, link, descrizioneTodo, immagine, immaginepath, colore, autore, bacheca, completato);

        // Chiama il metodo del DAO (salvaToDo)
        todoDAO.salvaToDo(nuovo, bacheca.getIdBa(), utenteCorrente);

        return nuovo;
    }

    /**
     * Modifica un ToDo esistente.
     */
    public void modificaToDo(ToDo todo, String nuovoTitolo, String nuovaDescrizione,
                             String nuovoLink, LocalDate nuovaScadenza,
                             Color nuovoColore, Image nuovaImmagine, String nuovaImmaginePath) {

        if (nuovoTitolo != null && !nuovoTitolo.trim().isEmpty()) {
            todo.setTitolo(nuovoTitolo);
        }
        todo.setDescrizione(nuovaDescrizione);
        todo.setUrl(nuovoLink);
        todo.setDatescadenza(nuovaScadenza);
        todo.setColor(nuovoColore);
        todo.setImage(nuovaImmagine);
        todo.setImaginepath(nuovaImmaginePath);

        // Aggiorna nel DB
        todoDAO.updateToDo(todo, todo.getIdToDo());
    }

    /**
     * Imposta manualmente lo stato di completamento di un ToDo.
     */
    public void setToDoCompletato(ToDo todo, Boolean stato) {
        todo.setCompletato(stato);
        // Aggiorna lo stato nel database
        todoDAO.updateToDo(todo, todo.getIdToDo());
    }

    /**
     * Elimina un ToDo (e le relative condivisioni/checklist) dal DB e dalla memoria.
     */
    public void eliminaToDo(ToDo todo, Bacheca bacheca, Utente utente) {
        // Rimuove prima le condivisioni lato DB
        condivisioneDAO.eliminaCondivisionePerUtente(todo.getIdToDo(), utente.getLogin());

        // Rimuove il ToDo dal DB (il DAO dovrebbe gestire la cancellazione a cascata delle checklist se impostato nel DB,
        // altrimenti bisognerebbe chiamare l'eliminazione checklist qui)
        todoDAO.eliminaToDo(todo, todo.getIdToDo());

        if (bacheca != null) {
            bacheca.getTodos().remove(todo);
        }
    }

    // --- GESTIONE CONDIVISIONI ---

    public void aggiungiCondivisione(Utente destinatario, ToDo todo) {
        // Carica le bacheche del destinatario per assicurarsi che siano in memoria se serve
        List<Bacheca> bacheche = bachecaDAO.getBachecaByUtente(destinatario.getLogin());
        if (bacheche.size() >= 3) {
            destinatario.setBacheca1(bacheche.get(0));
            destinatario.setBacheca2(bacheche.get(1));
            destinatario.setBacheca3(bacheche.get(2));
        }
        condivisioneDAO.aggiungiCondivisione(todo.getIdToDo(), destinatario.getLogin());
    }

    public List<Utente> getUtentiCondivisi(ToDo todo) {
        return condivisioneDAO.getUtentiCondivisi(todo.getIdToDo());
    }

    public boolean checkAutore(Utente autore, Utente richiedente) {
        return autore.getLogin().equals(richiedente.getLogin());
    }

    public void rimuoviCondivisione(Utente destinatario, ToDo todo, int numeroBacheca) {
        Bacheca bachecaDest = null;
        switch (numeroBacheca) {
            case 1 -> bachecaDest = destinatario.getBacheca1();
            case 2 -> bachecaDest = destinatario.getBacheca2();
            case 3 -> bachecaDest = destinatario.getBacheca3();
        }

        if (bachecaDest != null) {
            bachecaDest.getTodos().remove(todo);
        }
        todo.getCondivisioni().remove(destinatario);
        condivisioneDAO.eliminaCondivisionePerUtente(todo.getIdToDo(), destinatario.getLogin());
    }

    // --- GESTIONE CHECKLIST (Track 1) ---

    /**
     * Salva una nuova attività della checklist per un ToDo.
     */
    public void salvaCheckListAttivita(CheckList attivita, ToDo todo) {
        checklistDao.salvaCheckListAttivita(attivita, todo.getIdToDo());
        todo.addCheckListAttivita(attivita);

        // Ricontrolla lo stato (nel caso si aggiunga un'attività non completata a un todo completato)
        checkAndSetToDoCompletato(todo);
    }

    /**
     * Aggiorna lo stato di una voce della checklist e verifica se il ToDo deve essere completato.
     */
    public void updateStatoCheckList(CheckList attivita, boolean completato, ToDo todo) {
        // 1. Aggiorna DB
        checklistDao.updateStatoCheckList(attivita.getIdChecklist(), completato);
        // 2. Aggiorna Model
        attivita.setStato(completato);

        // 3. Logica Automatica: se tutte sono completate, completa il ToDo
        checkAndSetToDoCompletato(todo);
    }

    /**
     * Elimina una voce della checklist.
     */
    public void eliminaCheckListAttivita(CheckList attivita, ToDo todo) {
        checklistDao.eliminaCheckListAttivita(attivita.getIdChecklist());
        todo.getChecklist().remove(attivita);

        // Dopo l'eliminazione, potremmo aver rimosso l'unica voce non completata
        checkAndSetToDoCompletato(todo);
    }

    /**
     * Metodo logico che controlla se tutte le voci della checklist sono spuntate.
     * Se sì, setta il ToDo come completato.
     * Se no, e il ToDo era completato, lo rimette come da completare.
     */
    private void checkAndSetToDoCompletato(ToDo todo) {
        if (todo.getChecklist() == null || todo.getChecklist().isEmpty()) {
            return;
        }

        boolean tutteCompletate = true;
        for (CheckList c : todo.getChecklist()) {
            if (!c.getStato()) {
                tutteCompletate = false;
                break;
            }
        }

        boolean statoAttualeToDo = Boolean.TRUE.equals(todo.getCompletato());

        // Se tutte sono completate e il ToDo NON è ancora segnato completato
        if (tutteCompletate && !statoAttualeToDo) {
            todo.setCompletato(true);
            todoDAO.updateToDo(todo, todo.getIdToDo()); // Aggiorna DB ToDo
        }
        // Opzionale: Se NON sono tutte completate ma il ToDo risulta completato (riapertura task)
        else if (!tutteCompletate && statoAttualeToDo) {
            todo.setCompletato(false);
            todoDAO.updateToDo(todo, todo.getIdToDo()); // Aggiorna DB ToDo
        }
    }

    // --- ALTRE FUNZIONI ---

    public List<ToDo> getToDoEntroData(Utente utente, LocalDate dataLimite) {
        List<ToDo> risultati = new ArrayList<>();
        List<Bacheca> bacheche = Arrays.asList(utente.getBacheca1(), utente.getBacheca2(), utente.getBacheca3());

        for (Bacheca b : bacheche) {
            for (ToDo t : b.getTodos()) {
                if (t.getDatescadenza() != null &&
                        !t.getDatescadenza().isAfter(dataLimite) &&
                        !t.getDatescadenza().isBefore(LocalDate.now()) &&
                        Boolean.FALSE.equals(t.getCompletato())) {
                    risultati.add(t);
                }
            }
        }
        return risultati;
    }

    /**
     * Carica tutti i dati (ToDo, Condivisi e Checklist) per l'utente.
     */
    public void caricaDati(Utente utente) {
        // 1. Popola i ToDo personali nelle 3 bacheche
        todoDAO.popolabacheche(utente.getBacheca1().getIdBa(), utente.getBacheca1(), utente);
        todoDAO.popolabacheche(utente.getBacheca2().getIdBa(), utente.getBacheca2(), utente);
        todoDAO.popolabacheche(utente.getBacheca3().getIdBa(), utente.getBacheca3(), utente);

        // 2. Popola i ToDo condivisi nelle 3 bacheche
        todoDAO.popolaToDocondivisi(utente, utente.getBacheca1(), 1);
        todoDAO.popolaToDocondivisi(utente, utente.getBacheca2(), 2);
        todoDAO.popolaToDocondivisi(utente, utente.getBacheca3(), 3);

        // 3. Popola le CHECKLIST per ogni ToDo caricato
        List<ToDo> tuttiIToDo = new ArrayList<>();
        tuttiIToDo.addAll(utente.getBacheca1().getTodos());
        tuttiIToDo.addAll(utente.getBacheca2().getTodos());
        tuttiIToDo.addAll(utente.getBacheca3().getTodos());

        for (ToDo t : tuttiIToDo) {
            // Recupera dal DAO checklist
            List<CheckList> items = checklistDao.getCheckListByToDoId(t.getIdToDo());
            // Imposta nel Model (cast a ArrayList come da Model)
            t.setChecklist((ArrayList<CheckList>) items);
        }
    }

    public void salvaPosizione(ToDo todo, Bacheca bacheca) {
        todo.setBacheca(bacheca);
        if (todo.getAutore().getLogin().equals(utenteCorrente.getLogin())) {
            // Uso del metodo "cambiabacheca" come da interfaccia ToDoDao fornita
            todoDAO.cambiabacheca(todo, bacheca.getIdBa(), todo.getIdToDo());
        }
    }

    public boolean checkAutorePerSpostamento(ToDo todo, Utente utenteCorrente) {
        return todo.getAutore().getLogin().equals(utenteCorrente.getLogin());
    }

    public void eliminaBacheca(Bacheca bacheca) {
        condivisioneDAO.eliminaCondivisioniDellaBacheca(bacheca.getIdBa());
        // Uso del metodo "svuotabahcea" come da interfaccia ToDoDao fornita
        todoDAO.svuotabahcea(bacheca.getIdBa());
        bacheca.getTodos().clear();
    }

    public String getAutoreToDo(int idTodo) {
        // Uso del metodo "getautore" come da interfaccia ToDoDao fornita
        return todoDAO.getautore(idTodo);
    }

    public Utente getUtenteCorrente() {
        return utenteCorrente;
    }
}