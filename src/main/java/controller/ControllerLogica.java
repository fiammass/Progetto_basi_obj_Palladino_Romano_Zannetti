package controller;

import dao.*;
import implementazioni.postgres.dao.*;
import model.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Classe che gestisce la logica di business dell'applicazione ("Model Controller").
 * Funge da intermediario tra il controller dell'interfaccia grafica ({@link ControllerGui})
 * e il livello di persistenza dei dati (DAO).
 * <p>
 * Si occupa di:
 * <ul>
 * <li>Gestione autenticazione e registrazione utenti.</li>
 * <li>Inizializzazione e caricamento dati (Bacheche, ToDo, Condivisioni).</li>
 * <li>Operazioni CRUD su ToDo e Checklist.</li>
 * <li>Logiche automatiche (es. completamento ToDo se checklist finita).</li>
 * </ul>
 */
public class ControllerLogica {

    /** Nome standard per la bacheca universitaria. */
    public static final String BACHECA_UNI = "Università";
    /** Nome standard per la bacheca lavorativa. */
    public static final String BACHECA_LAVORO = "Lavoro";
    /** Nome standard per la bacheca di svago. */
    public static final String BACHECA_SVAGO = "Tempo Libero";

    private Utente utenteCorrente;

    private final UtenteDao utenteDAO;
    private final ToDoDao todoDAO;
    private final BachecaDao bachecaDAO;
    private final ChecklistDao checklistDao;
    private final CondivisioneDAO condivisioneDAO;

    /**
     * Costruttore della classe.
     * Inizializza tutte le implementazioni dei DAO per l'accesso al database PostgreSQL.
     */
    public ControllerLogica() {
        this.utenteDAO = new UtenteImplementazione();
        this.todoDAO = new ToDoImplementazione();
        this.bachecaDAO = new BachecaImplementazione();
        this.checklistDao = new CheckListImplementazione();
        this.condivisioneDAO = new CondivisioneImplementazionePostgreDAO();
    }

    /**
     * Verifica le credenziali di accesso.
     * Se corrette, carica l'utente e le sue bacheche, popolando tutti i dati necessari.
     *
     * @param username Lo username dell'utente.
     * @param password La password dell'utente.
     * @return true se l'autenticazione ha successo, false altrimenti.
     */
    public boolean checkLogin(String username, String password) {
        if (utenteDAO.VerificaLogin(username, password)) {
            utenteCorrente = utenteDAO.getUtentebyUsername(username);
            List<Bacheca> bacheche = bachecaDAO.getBachecaByUtente(username);
            if (bacheche != null && bacheche.size() >= 3) {
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

    /**
     * Registra un nuovo utente nel sistema e crea automaticamente le tre bacheche di default.
     *
     * @param username Lo username scelto.
     * @param password La password scelta.
     * @return L'oggetto Utente appena creato e configurato.
     */
    public Utente aggiungiUtente(String username, String password) {
        Utente utente = new Utente(username, password);
        utenteDAO.salvautente(utente);
        bachecaDAO.creaBacheca(BACHECA_UNI, "Bacheca universitaria", username, 1);
        bachecaDAO.creaBacheca(BACHECA_LAVORO, "Bacheca lavorativa", username, 2);
        bachecaDAO.creaBacheca(BACHECA_SVAGO, "Bacheca svago", username, 3);

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

    /**
     * Carica tutti i dati associati a un utente:
     * <ul>
     * <li>ToDo proprietari presenti nelle bacheche.</li>
     * <li>ToDo condivisi da altri utenti.</li>
     * <li>Dettagli di ogni ToDo (Checklist e lista utenti condivisi).</li>
     * </ul>
     *
     * @param utente L'utente per il quale caricare i dati.
     */
    public void caricaDati(Utente utente) {
        todoDAO.popolabacheche(utente.getBacheca1().getIdBa(), utente.getBacheca1(), utente);
        todoDAO.popolabacheche(utente.getBacheca2().getIdBa(), utente.getBacheca2(), utente);
        todoDAO.popolabacheche(utente.getBacheca3().getIdBa(), utente.getBacheca3(), utente);

        todoDAO.popolaToDocondivisi(utente, utente.getBacheca1(), 1);
        todoDAO.popolaToDocondivisi(utente, utente.getBacheca2(), 2);
        todoDAO.popolaToDocondivisi(utente, utente.getBacheca3(), 3);

        List<ToDo> tutti = new ArrayList<>();
        if(utente.getBacheca1().getTodos() != null) tutti.addAll(utente.getBacheca1().getTodos());
        if(utente.getBacheca2().getTodos() != null) tutti.addAll(utente.getBacheca2().getTodos());
        if(utente.getBacheca3().getTodos() != null) tutti.addAll(utente.getBacheca3().getTodos());

        for (ToDo t : tutti) {
            List<CheckList> items = checklistDao.getCheckListByToDoId(t.getIdToDo());
            t.setChecklist(items != null ? new ArrayList<>(items) : new ArrayList<>());

            List<Utente> utentiCondivisi = condivisioneDAO.getUtentiCondivisi(t.getIdToDo());
            t.setCondivisioni((ArrayList<Utente>) utentiCondivisi);
        }
    }

    /**
     * Condivide un ToDo con un altro utente.
     * Verifica che l'utente destinatario esista e non sia l'utente corrente.
     *
     * @param todo                 Il ToDo da condividere.
     * @param usernameDestinatario Lo username dell'utente con cui condividere.
     * @return true se la condivisione ha successo, false altrimenti.
     */
    public boolean condividiToDo(ToDo todo, String usernameDestinatario) {
        if (usernameDestinatario.equals(utenteCorrente.getlogin())) return false;

        Utente dest = utenteDAO.getUtentebyUsername(usernameDestinatario);
        if (dest == null) return false;

        condivisioneDAO.aggiungiCondivisione(todo.getIdToDo(), usernameDestinatario);
        todo.getCondivisioni().add(dest);
        return true;
    }

    /**
     * Rimuove la condivisione di un ToDo con uno specifico utente.
     *
     * @param todo                Il ToDo in questione.
     * @param usernameDaRimuovere Lo username dell'utente da rimuovere.
     */
    public void rimuoviCondivisione(ToDo todo, String usernameDaRimuovere) {
        condivisioneDAO.eliminaCondivisionePerUtente(todo.getIdToDo(), usernameDaRimuovere);
        todo.getCondivisioni().removeIf(u -> u.getlogin().equals(usernameDaRimuovere));
    }

    /**
     * Crea un nuovo ToDo, lo salva nel database e lo associa alla bacheca specificata.
     *
     * @param titolo      Titolo del ToDo.
     * @param scadenza    Data di scadenza.
     * @param link        Link URL associato.
     * @param descrizione Descrizione testuale.
     * @param immagine    Oggetto immagine.
     * @param immPath     Percorso file immagine.
     * @param colore      Colore di sfondo.
     * @param autore      Autore del ToDo.
     * @param bacheca     Bacheca di destinazione.
     * @param completato  Stato di completamento iniziale.
     * @return L'oggetto ToDo creato e salvato (con ID generato), oppure null se il titolo è invalido.
     */
    public ToDo createToDo(String titolo, LocalDate scadenza, String link, String descrizione,
                           Image immagine, String immPath, Color colore,
                           Utente autore, Bacheca bacheca, Boolean completato) {
        if (titolo == null || titolo.trim().isEmpty()) return null;
        ToDo nuovo = new ToDo(titolo, scadenza, link, descrizione, immagine, immPath, colore, autore, bacheca, completato);
        todoDAO.salvaToDo(nuovo, bacheca.getIdBa(), utenteCorrente);
        return nuovo;
    }

    /**
     * Aggiorna i dati di un ToDo esistente nel database.
     *
     * @param todo     Il ToDo da modificare.
     * @param titolo   Nuovo titolo.
     * @param desc     Nuova descrizione.
     * @param link     Nuovo link.
     * @param scadenza Nuova data di scadenza.
     * @param colore   Nuovo colore.
     * @param img      Nuova immagine.
     * @param imgPath  Nuovo percorso immagine.
     */
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

    /**
     * Elimina un ToDo dal database e dalla memoria locale.
     *
     * @param todo Il ToDo da eliminare.
     */
    public void eliminaToDo(ToDo todo) {
        if (todo != null && todo.getIdToDo() != null) {
            todoDAO.eliminaToDo(todo, todo.getIdToDo());
            if (todo.getBacheca() != null && todo.getBacheca().getTodos() != null) {
                todo.getBacheca().getTodos().remove(todo);
            }
        }
    }

    /**
     * Restituisce l'utente attualmente loggato nel sistema.
     *
     * @return L'oggetto Utente corrente.
     */
    public Utente getUtenteCorrente() { return utenteCorrente; }

    /**
     * Cerca un ToDo tramite il titolo all'interno di tutte le bacheche dell'utente.
     *
     * @param titolo Il titolo da cercare (case insensitive).
     * @return Il ToDo trovato, oppure null se non esiste.
     */
    public ToDo searchToDo(String titolo) {
        List<Bacheca> bacheche = Arrays.asList(utenteCorrente.getBacheca1(), utenteCorrente.getBacheca2(), utenteCorrente.getBacheca3());
        for (Bacheca b : bacheche) {
            if (b.getTodos() != null) {
                for (ToDo t : b.getTodos()) {
                    if (t.getTitolo().equalsIgnoreCase(titolo)) return t;
                }
            }
        }
        return null;
    }

    /**
     * Recupera una lista di ToDo non ancora completati che scadono entro una data specifica.
     *
     * @param utente     L'utente su cui effettuare il controllo.
     * @param dataLimite La data limite entro cui cercare le scadenze.
     * @return Una lista di ToDo in scadenza.
     */
    public List<ToDo> getToDoEntroData(Utente utente, LocalDate dataLimite) {
        List<ToDo> risultati = new ArrayList<>();
        List<Bacheca> bacheche = Arrays.asList(utente.getBacheca1(), utente.getBacheca2(), utente.getBacheca3());
        for (Bacheca b : bacheche) {
            if (b.getTodos() != null) {
                for (ToDo t : b.getTodos()) {
                    if (t.getDatescadenza() != null &&
                            !t.getDatescadenza().isAfter(dataLimite) &&
                            !t.getDatescadenza().isBefore(LocalDate.now()) &&
                            Boolean.FALSE.equals(t.getCompletato())) {
                        risultati.add(t);
                    }
                }
            }
        }
        return risultati;
    }

    /**
     * Aggiunge un'attività alla checklist di un ToDo e salva nel DB.
     * Controlla automaticamente se il ToDo deve essere segnato come completato.
     *
     * @param attivita L'attività da aggiungere.
     * @param todo     Il ToDo padre.
     */
    public void salvaCheckListAttivita(CheckList attivita, ToDo todo) {
        checklistDao.salvaCheckListAttivita(attivita, todo.getIdToDo());
        todo.addCheckListAttivita(attivita);
        checkAndSetToDoCompletato(todo);
    }

    /**
     * Aggiorna lo stato di un'attività della checklist nel DB.
     * Verifica se il cambiamento di stato influenza il completamento del ToDo.
     *
     * @param attivita   L'attività da aggiornare.
     * @param completato Il nuovo stato.
     * @param todo       Il ToDo padre.
     */
    public void updateStatoCheckList(CheckList attivita, boolean completato, ToDo todo) {
        checklistDao.updateStatoCheckList(attivita.getIdChecklist(), completato);
        attivita.setStato(completato);
        checkAndSetToDoCompletato(todo);
    }

    /**
     * Elimina un'attività dalla checklist nel DB.
     * Verifica se la rimozione influenza il completamento del ToDo.
     *
     * @param attivita L'attività da rimuovere.
     * @param todo     Il ToDo padre.
     */
    public void eliminaCheckListAttivita(CheckList attivita, ToDo todo) {
        checklistDao.eliminaCheckListAttivita(attivita.getIdChecklist());
        todo.getChecklist().remove(attivita);
        checkAndSetToDoCompletato(todo);
    }

    /**
     * Metodo privato che implementa la logica di business per l'autocompletamento.
     * Se tutte le voci della checklist sono completate, il ToDo viene segnato come completato.
     * Se una voce viene deselezionata, il ToDo torna non completato.
     *
     * @param todo Il ToDo da verificare.
     */
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
}