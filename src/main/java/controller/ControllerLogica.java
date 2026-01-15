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
 * Controller Logico che gestisce le regole di business dell'applicazione.
 * Questa classe funge da intermediario tra l'interfaccia grafica (ControllerGui)
 * e il livello di accesso ai dati (DAO).
 * Gestisce la logica di login, caricamento dati, manipolazione dei ToDo e delle Checklist.
 */
public class ControllerLogica {

    // Costanti per i nomi delle bacheche predefinite
    public static final String BACHECA_UNI = "Università";
    public static final String BACHECA_LAVORO = "Lavoro";
    public static final String BACHECA_SVAGO = "Tempo Libero";

    private Utente utenteCorrente;

    // Riferimenti ai Data Access Objects (DAO)
    private final UtenteDao utenteDAO;
    private final ToDoDao todoDAO;
    private final BachecaDao bachecaDAO;
    private final CondivisioneDAO condivisioneDAO;
    private final ChecklistDao checklistDao;

    /**
     * Costruttore: Inizializza tutte le implementazioni dei DAO.
     */
    public ControllerLogica() {
        this.utenteDAO = new UtenteImplementazione();
        this.todoDAO = new ToDoImplementazione();
        this.bachecaDAO = new BachecaImplementazione();
        this.condivisioneDAO = new CondivisioneImplementazionePostgreDAO();
        this.checklistDao = new CheckListImplementazione();
    }

    // =================================================================================
    // SEZIONE: GESTIONE UTENTE E LOGIN
    // =================================================================================

    /**
     * Verifica le credenziali di login. Se corrette, carica tutti i dati dell'utente.
     *
     * @param username Lo username inserito.
     * @param password La password inserita.
     * @return true se il login ha successo, false altrimenti.
     */
    public boolean checkLogin(String username, String password) {
        if (utenteDAO.VerificaLogin(username, password)) {
            utenteCorrente = utenteDAO.getUtentebyUsername(username);

            // Recupera le bacheche dal DB
            List<Bacheca> bacheche = bachecaDAO.getBachecaByUtente(username);

            if (bacheche != null && bacheche.size() >= 3) {
                // Assegna le bacheche all'oggetto Utente in base al titolo
                for (Bacheca b : bacheche) {
                    if (b.getTitolo().equals(BACHECA_UNI)) utenteCorrente.setBacheca1(b);
                    else if (b.getTitolo().equals(BACHECA_LAVORO)) utenteCorrente.setBacheca2(b);
                    else if (b.getTitolo().equals(BACHECA_SVAGO)) utenteCorrente.setBacheca3(b);
                }
                // Carica i ToDo e le Checklist
                caricaDati(utenteCorrente);
                return true;
            }
        }
        return false;
    }

    /**
     * Registra un nuovo utente e crea le sue 3 bacheche di default.
     *
     * @param username Lo username scelto.
     * @param password La password scelta.
     * @return L'oggetto Utente appena creato.
     */
    public Utente aggiungiUtente(String username, String password) {
        Utente utente = new Utente(username, password);
        utenteDAO.salvautente(utente);

        // Crea le 3 bacheche standard nel DB
        bachecaDAO.creaBacheca(BACHECA_UNI, "Bacheca universitaria", username, 1);
        bachecaDAO.creaBacheca(BACHECA_LAVORO, "Bacheca lavorativa", username, 2);
        bachecaDAO.creaBacheca(BACHECA_SVAGO, "Bacheca svago", username, 3);

        // Rilegge dal DB per avere gli ID corretti e assegnarli all'utente in memoria
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
     * Carica tutti i ToDo e le relative Checklist per l'utente loggato.
     *
     * @param utente L'utente per cui caricare i dati.
     */
    public void caricaDati(Utente utente) {
        // 1. Popola i ToDo nelle bacheche
        todoDAO.popolabacheche(utente.getBacheca1().getIdBa(), utente.getBacheca1(), utente);
        todoDAO.popolabacheche(utente.getBacheca2().getIdBa(), utente.getBacheca2(), utente);
        todoDAO.popolabacheche(utente.getBacheca3().getIdBa(), utente.getBacheca3(), utente);

        // 2. Popola i ToDo condivisi (se presenti)
        todoDAO.popolaToDocondivisi(utente, utente.getBacheca1(), 1);
        todoDAO.popolaToDocondivisi(utente, utente.getBacheca2(), 2);
        todoDAO.popolaToDocondivisi(utente, utente.getBacheca3(), 3);

        // 3. Popola le Checklist per ogni singolo ToDo caricato
        List<ToDo> tuttiIToDo = new ArrayList<>();
        if (utente.getBacheca1().getTodos() != null) tuttiIToDo.addAll(utente.getBacheca1().getTodos());
        if (utente.getBacheca2().getTodos() != null) tuttiIToDo.addAll(utente.getBacheca2().getTodos());
        if (utente.getBacheca3().getTodos() != null) tuttiIToDo.addAll(utente.getBacheca3().getTodos());

        for (ToDo t : tuttiIToDo) {
            List<CheckList> items = checklistDao.getCheckListByToDoId(t.getIdToDo());
            // Assicuriamoci che la lista non sia null
            t.setChecklist(items != null ? new ArrayList<>(items) : new ArrayList<>());
        }
    }

    /**
     * Restituisce l'utente attualmente loggato.
     * @return L'oggetto Utente.
     */
    public Utente getUtenteCorrente() {
        return utenteCorrente;
    }

    // =================================================================================
    // SEZIONE: GESTIONE TODO
    // =================================================================================

    /**
     * Crea un nuovo ToDo e lo salva nel database.
     *
     * @param titolo Titolo del ToDo.
     * @param scadenza Data di scadenza.
     * @param link Link opzionale.
     * @param descrizione Descrizione testuale.
     * @param immagine Oggetto immagine (opzionale).
     * @param immPath Percorso immagine (opzionale).
     * @param colore Colore di sfondo.
     * @param autore Autore del ToDo.
     * @param bacheca Bacheca di destinazione.
     * @param completato Stato iniziale.
     * @return Il nuovo oggetto ToDo creato (con ID assegnato dal DAO).
     */
    public ToDo createToDo(String titolo, LocalDate scadenza, String link, String descrizione,
                           Image immagine, String immPath, Color colore,
                           Utente autore, Bacheca bacheca, Boolean completato) {

        if (titolo == null || titolo.trim().isEmpty()) return null;

        ToDo nuovo = new ToDo(titolo, scadenza, link, descrizione, immagine, immPath, colore, autore, bacheca, completato);
        // Il DAO si occupa di salvare e settare l'ID generato nell'oggetto 'nuovo'
        todoDAO.salvaToDo(nuovo, bacheca.getIdBa(), utenteCorrente);
        return nuovo;
    }

    /**
     * Modifica un ToDo esistente nel database.
     *
     * @param todo L'oggetto ToDo da modificare.
     * @param titolo Nuovo titolo.
     * @param desc Nuova descrizione.
     * @param link Nuovo link.
     * @param scadenza Nuova data.
     * @param colore Nuovo colore.
     * @param img Nuova immagine.
     * @param imgPath Nuovo path immagine.
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
            // Rimuove dal DB
            todoDAO.eliminaToDo(todo, todo.getIdToDo());

            // Rimuove dalla lista in memoria della bacheca
            if (todo.getBacheca() != null && todo.getBacheca().getTodos() != null) {
                todo.getBacheca().getTodos().remove(todo);
            }
        }
    }

    /**
     * Cerca un ToDo per titolo in tutte le bacheche dell'utente.
     *
     * @param titolo Il titolo da cercare.
     * @return Il primo ToDo trovato o null.
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
     * Recupera la lista dei ToDo che scadono entro una certa data.
     *
     * @param utente L'utente corrente.
     * @param dataLimite La data entro cui verificare la scadenza (es. oggi).
     * @return Lista di ToDo in scadenza.
     */
    public List<ToDo> getToDoEntroData(Utente utente, LocalDate dataLimite) {
        List<ToDo> risultati = new ArrayList<>();
        List<Bacheca> bacheche = Arrays.asList(utente.getBacheca1(), utente.getBacheca2(), utente.getBacheca3());

        for (Bacheca b : bacheche) {
            if (b.getTodos() != null) {
                for (ToDo t : b.getTodos()) {
                    if (t.getDatescadenza() != null &&
                            !t.getDatescadenza().isAfter(dataLimite) && // Non dopo la data limite
                            !t.getDatescadenza().isBefore(LocalDate.now()) && // Non nel passato (già scaduti ieri)
                            Boolean.FALSE.equals(t.getCompletato())) { // Solo quelli non fatti
                        risultati.add(t);
                    }
                }
            }
        }
        return risultati;
    }

    // =================================================================================
    // SEZIONE: GESTIONE CHECKLIST
    // =================================================================================

    /**
     * Salva una nuova attività nella checklist di un ToDo.
     * Controlla automaticamente se questo completa il ToDo.
     *
     * @param attivita L'attività da aggiungere.
     * @param todo Il ToDo padre.
     */
    public void salvaCheckListAttivita(CheckList attivita, ToDo todo) {
        checklistDao.salvaCheckListAttivita(attivita, todo.getIdToDo());
        todo.addCheckListAttivita(attivita);
        checkAndSetToDoCompletato(todo);
    }

    /**
     * Aggiorna lo stato di un'attività della checklist.
     * Controlla automaticamente se questo completa o riapre il ToDo.
     *
     * @param attivita L'attività da aggiornare.
     * @param completato Nuovo stato (true/false).
     * @param todo Il ToDo padre.
     */
    public void updateStatoCheckList(CheckList attivita, boolean completato, ToDo todo) {
        checklistDao.updateStatoCheckList(attivita.getIdChecklist(), completato);
        attivita.setStato(completato);
        checkAndSetToDoCompletato(todo);
    }

    /**
     * Elimina un'attività dalla checklist.
     * Controlla automaticamente se la rimozione influenza lo stato del ToDo.
     *
     * @param attivita L'attività da eliminare.
     * @param todo Il ToDo padre.
     */
    public void eliminaCheckListAttivita(CheckList attivita, ToDo todo) {
        checklistDao.eliminaCheckListAttivita(attivita.getIdChecklist());
        todo.getChecklist().remove(attivita);
        checkAndSetToDoCompletato(todo);
    }

    /**
     * LOGICA CORE: Controlla lo stato della checklist e aggiorna il ToDo padre.
     * Se tutte le voci sono spuntate -> Il ToDo diventa Completato.
     * Se una voce viene deselezionata -> Il ToDo torna Non Completato.
     *
     * @param todo Il ToDo da verificare.
     */
    private void checkAndSetToDoCompletato(ToDo todo) {
        // Se non c'è checklist, non facciamo nulla
        if (todo.getChecklist() == null || todo.getChecklist().isEmpty()) {
            return;
        }

        // Verifica se tutte le attività sono true
        boolean tutteCompletate = todo.getChecklist().stream().allMatch(CheckList::getStato);
        boolean statoAttualeToDo = Boolean.TRUE.equals(todo.getCompletato());

        if (tutteCompletate && !statoAttualeToDo) {
            // Tutte fatte, ma ToDo ancora aperto -> CHIUDI
            todo.setCompletato(true);
            todoDAO.updateToDo(todo, todo.getIdToDo());
            System.out.println("LOGICA: ToDo completato automaticamente (Checklist finita).");
        } else if (!tutteCompletate && statoAttualeToDo) {
            // Qualcosa non fatto, ma ToDo risulta chiuso -> RIAPRI
            todo.setCompletato(false);
            todoDAO.updateToDo(todo, todo.getIdToDo());
            System.out.println("LOGICA: ToDo riaperto automaticamente (Checklist incompleta).");
        }
    }
}