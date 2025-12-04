package controller;

import implementazioni.postgres.dao.*;
import dao.*;
import model.*;

import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ControllerLogica {

    // Costanti per evitare "Magic Strings"
    public static final String BACHECA_UNI = "Università";
    public static final String BACHECA_LAVORO = "Lavoro";
    public static final String BACHECA_SVAGO = "Tempo Libero";

    private Utente utenteCorrente;

    // DAO Interfaces
    private final UtenteDao utenteDAO;
    private final ToDoDao todoDAO;
    private final BachecaDao bachecaDAO;
    private final CondivisioneDAO condivisioneDAO;
    private final ChecklistDao checklistDao;

    public ControllerLogica() {
        // Inizializzazione DAO
        this.utenteDAO = new UtenteImplementazione();
        this.todoDAO = new ToDoImplementazione();
        this.bachecaDAO = new BachecaImplementazione();
        this.condivisioneDAO = new CondivisioneImplementazionePostgreDAO();
        this.checklistDao = new CheckListImplementazione();
    }

    public boolean checkLogin(String username, String password) {
        if (utenteDAO.VerificaLogin(username, password)) {
            utenteCorrente = utenteDAO.getUtentebyUsername(username);
            List<Bacheca> bacheche = bachecaDAO.getBachecaByUtente(username);

            if (bacheche != null && bacheche.size() >= 3) {
                // Mappa le bacheche in base al titolo (più sicuro dell'indice)
                for (Bacheca b : bacheche) {
                    if (b.getTitolo().equals(BACHECA_UNI)) utenteCorrente.setBacheca1(b);
                    else if (b.getTitolo().equals(BACHECA_LAVORO)) utenteCorrente.setBacheca2(b);
                    else if (b.getTitolo().equals(BACHECA_SVAGO)) utenteCorrente.setBacheca3(b);
                }
                caricaDati(utenteCorrente);
                return true;
            }
        }
        return false;
    }

    public Utente aggiungiUtente(String username, String password) {
        Utente utente = new Utente(username, password);
        utenteDAO.salvautente(utente);

        // Usa le costanti per creare le bacheche
        bachecaDAO.creaBacheca(BACHECA_UNI, "Bacheca universitaria", username, 1);
        bachecaDAO.creaBacheca(BACHECA_LAVORO, "Bacheca lavorativa", username, 2);
        bachecaDAO.creaBacheca(BACHECA_SVAGO, "Bacheca svago", username, 3);

        // Recupera e assegna
        List<Bacheca> bachecheDb = bachecaDAO.getBachecaByUtente(username);
        if (bachecheDb != null) {
            for (Bacheca bDb : bachecheDb) {
                if (bDb.getTitolo().equals(BACHECA_UNI)) utente.setBacheca1(bDb);
                else if (bDb.getTitolo().equals(BACHECA_LAVORO)) utente.setBacheca2(bDb);
                else if (bDb.getTitolo().equals(BACHECA_SVAGO)) utente.setBacheca3(bDb);
            }
        }
        return utente;
    }

    public void caricaDati(Utente utente) {
        // 1. Popola ToDo personali
        todoDAO.popolabacheche(utente.getBacheca1().getIdBa(), utente.getBacheca1(), utente);
        todoDAO.popolabacheche(utente.getBacheca2().getIdBa(), utente.getBacheca2(), utente);
        todoDAO.popolabacheche(utente.getBacheca3().getIdBa(), utente.getBacheca3(), utente);

        // 2. Popola ToDo condivisi
        todoDAO.popolaToDocondivisi(utente, utente.getBacheca1(), 1);
        todoDAO.popolaToDocondivisi(utente, utente.getBacheca2(), 2);
        todoDAO.popolaToDocondivisi(utente, utente.getBacheca3(), 3);

        // 3. Popola Checklist (Fix ClassCastException)
        List<ToDo> tuttiIToDo = new ArrayList<>();
        tuttiIToDo.addAll(utente.getBacheca1().getTodos());
        tuttiIToDo.addAll(utente.getBacheca2().getTodos());
        tuttiIToDo.addAll(utente.getBacheca3().getTodos());

        for (ToDo t : tuttiIToDo) {
            List<CheckList> items = checklistDao.getCheckListByToDoId(t.getIdToDo());
            // FIX: Creiamo una nuova ArrayList invece di castare brutalmente
            t.setChecklist(items != null ? new ArrayList<>(items) : new ArrayList<>());
        }
    }

    public ToDo createToDo(String titolo, LocalDate scadenza, String link, String descrizione,
                           Image immagine, String immPath, Color colore,
                           Utente autore, Bacheca bacheca, Boolean completato) {

        if (titolo == null || titolo.trim().isEmpty()) return null;

        ToDo nuovo = new ToDo(titolo, scadenza, link, descrizione, immagine, immPath, colore, autore, bacheca, completato);
        todoDAO.salvaToDo(nuovo, bacheca.getIdBa(), utenteCorrente);
        return nuovo;
    }

    public void modificaToDo(ToDo todo, String titolo, String desc, String link,
                             LocalDate scadenza, Color colore, Image img, String imgPath) {
        if (titolo != null && !titolo.trim().isEmpty()) todo.setTitolo(titolo);
        todo.setDescrizione(desc);
        todo.setUrl(link);
        todo.setDatescadenza(scadenza);
        todo.setColor(colore);
        todo.setImage(img);
        todo.setImaginepath(imgPath);
        todoDAO.updateToDo(todo, todo.getIdToDo());
    }

    // --- CHECKLIST ---
    public void salvaCheckListAttivita(CheckList attivita, ToDo todo) {
        checklistDao.salvaCheckListAttivita(attivita, todo.getIdToDo());
        todo.addCheckListAttivita(attivita);
        checkAndSetToDoCompletato(todo);
    }

    public void updateStatoCheckList(CheckList attivita, boolean completato, ToDo todo) {
        checklistDao.updateStatoCheckList(attivita.getIdChecklist(), completato);
        attivita.setStato(completato);
        checkAndSetToDoCompletato(todo);
    }

    public void eliminaCheckListAttivita(CheckList attivita, ToDo todo) {
        checklistDao.eliminaCheckListAttivita(attivita.getIdChecklist());
        todo.getChecklist().remove(attivita);
        checkAndSetToDoCompletato(todo);
    }

    private void checkAndSetToDoCompletato(ToDo todo) {
        if (todo.getChecklist() == null || todo.getChecklist().isEmpty()) return;

        boolean tutteCompletate = todo.getChecklist().stream().allMatch(CheckList::getStato);
        boolean statoAttuale = Boolean.TRUE.equals(todo.getCompletato());

        if (tutteCompletate && !statoAttuale) {
            todo.setCompletato(true);
            todoDAO.updateToDo(todo, todo.getIdToDo());
        } else if (!tutteCompletate && statoAttuale) {
            todo.setCompletato(false);
            todoDAO.updateToDo(todo, todo.getIdToDo());
        }
    }

    // --- UTILS & GETTERS ---
    public Utente getUtenteCorrente() { return utenteCorrente; }

    public ToDo searchToDo(String titolo) {
        List<Bacheca> bacheche = Arrays.asList(utenteCorrente.getBacheca1(), utenteCorrente.getBacheca2(), utenteCorrente.getBacheca3());
        for (Bacheca b : bacheche) {
            for (ToDo t : b.getTodos()) {
                if (t.getTitolo().equalsIgnoreCase(titolo)) return t;
            }
        }
        return null;
    }

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
}