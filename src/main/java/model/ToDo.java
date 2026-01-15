package model;

import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Rappresenta un'attività (Task) all'interno del sistema.
 *
 * <p>
 * Un ToDo è l'entità principale dell'applicazione e contiene:
 * <ul>
 * <li>Dati descrittivi (Titolo, Descrizione, URL, Immagine).</li>
 * <li>Dati temporali (Data di scadenza).</li>
 * <li>Stato (Completato/Non completato).</li>
 * <li>Dati visivi (Colore dello sfondo della card).</li>
 * <li>Relazioni (Autore, Bacheca di appartenenza).</li>
 * <li>Sotto-elementi (Checklist di attività e Lista di utenti condivisi).</li>
 * </ul>
 */
public class ToDo {

    private Integer idToDo;
    private String titolo;
    private LocalDate datescadenza;
    private String url;
    private String descrizione;
    private Image image;
    private String imaginepath;
    private Color color;
    private Boolean completato = false;
    private Utente autore;
    private Bacheca bacheca;

    private ArrayList<Utente> condivisioni;
    private ArrayList<CheckList> checklist;

    /**
     * Costruttore della classe ToDo.
     * Inizializza l'oggetto e le liste interne (condivisioni e checklist).
     * Se viene fornita una bacheca, il ToDo si aggiunge automaticamente alla lista dei ToDo della bacheca.
     *
     * @param titolo       Il titolo del ToDo.
     * @param datescadenza La data di scadenza (può essere null).
     * @param url          Un link URL opzionale.
     * @param descrizione  Una descrizione testuale.
     * @param image        L'oggetto immagine in memoria.
     * @param imaginepath  Il percorso del file immagine su disco.
     * @param color        Il colore di sfondo per la visualizzazione grafica.
     * @param autore       L'utente creatore del ToDo.
     * @param bacheca      La bacheca a cui appartiene il ToDo.
     * @param completato   Lo stato iniziale di completamento.
     */
    public ToDo(String titolo, LocalDate datescadenza, String url, String descrizione,
                Image image, String imaginepath, Color color,
                Utente autore, Bacheca bacheca, Boolean completato) {

        this.titolo = titolo;
        this.datescadenza = datescadenza;
        this.url = url;
        this.descrizione = descrizione;
        this.image = image;
        this.imaginepath = imaginepath;
        this.color = color;
        this.autore = autore;
        this.bacheca = bacheca;
        this.completato = completato;

        this.condivisioni = new ArrayList<>();
        this.checklist = new ArrayList<>();

        if (bacheca != null && bacheca.getTodos() != null) {
            bacheca.getTodos().add(this);
        }
    }

    /**
     * Restituisce l'ID univoco del ToDo.
     *
     * @return L'ID intero.
     */
    public Integer getIdToDo() {
        return idToDo;
    }

    /**
     * Imposta l'ID univoco del ToDo (solitamente generato dal database).
     *
     * @param idToDo Il nuovo ID.
     */
    public void setIdToDo(Integer idToDo) {
        this.idToDo = idToDo;
    }

    /**
     * Restituisce il titolo del ToDo.
     *
     * @return La stringa del titolo.
     */
    public String getTitolo() {
        return titolo;
    }

    /**
     * Imposta il titolo del ToDo.
     *
     * @param titolo Il nuovo titolo.
     */
    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    /**
     * Restituisce la data di scadenza.
     *
     * @return La data di scadenza o null se non impostata.
     */
    public LocalDate getDatescadenza() {
        return datescadenza;
    }

    /**
     * Imposta la data di scadenza.
     *
     * @param datescadenza La nuova data.
     */
    public void setDatescadenza(LocalDate datescadenza) {
        this.datescadenza = datescadenza;
    }

    /**
     * Restituisce l'URL associato al ToDo.
     *
     * @return La stringa dell'URL.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Imposta l'URL associato.
     *
     * @param url Il nuovo URL.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Restituisce la descrizione dettagliata.
     *
     * @return La descrizione.
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * Imposta la descrizione.
     *
     * @param descrizione La nuova descrizione.
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * Restituisce l'immagine associata.
     *
     * @return L'oggetto Image.
     */
    public Image getImage() {
        return image;
    }

    /**
     * Imposta l'immagine associata.
     *
     * @param image La nuova immagine.
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * Restituisce il percorso su disco dell'immagine.
     *
     * @return Il percorso come stringa.
     */
    public String getImaginepath() {
        return imaginepath;
    }

    /**
     * Imposta il percorso dell'immagine.
     *
     * @param imaginepath Il nuovo percorso.
     */
    public void setImaginepath(String imaginepath) {
        this.imaginepath = imaginepath;
    }

    /**
     * Restituisce il colore di sfondo preferito per la visualizzazione.
     *
     * @return L'oggetto Color.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Imposta il colore di sfondo.
     *
     * @param color Il nuovo colore.
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Restituisce lo stato di completamento del ToDo.
     *
     * @return true se completato, false altrimenti.
     */
    public Boolean getCompletato() {
        return completato;
    }

    /**
     * Imposta lo stato di completamento.
     *
     * @param completato Il nuovo stato.
     */
    public void setCompletato(Boolean completato) {
        this.completato = completato;
    }

    /**
     * Restituisce l'autore (proprietario) del ToDo.
     *
     * @return L'oggetto Utente autore.
     */
    public Utente getAutore() {
        return autore;
    }

    /**
     * Imposta l'autore del ToDo.
     *
     * @param autore Il nuovo autore.
     */
    public void setAutore(Utente autore) {
        this.autore = autore;
    }

    /**
     * Restituisce la bacheca in cui è contenuto il ToDo.
     *
     * @return L'oggetto Bacheca.
     */
    public Bacheca getBacheca() {
        return bacheca;
    }

    /**
     * Imposta la bacheca di appartenenza.
     *
     * @param bacheca La nuova bacheca.
     */
    public void setBacheca(Bacheca bacheca) {
        this.bacheca = bacheca;
    }

    /**
     * Restituisce la lista degli utenti con cui questo ToDo è condiviso.
     *
     * @return Una lista di oggetti Utente.
     */
    public ArrayList<Utente> getCondivisioni() {
        return condivisioni;
    }

    /**
     * Imposta la lista degli utenti condivisi.
     *
     * @param condivisioni La nuova lista di condivisioni.
     */
    public void setCondivisioni(ArrayList<Utente> condivisioni) {
        this.condivisioni = condivisioni;
    }

    /**
     * Restituisce la lista delle sotto-attività (Checklist).
     *
     * @return Una lista di oggetti CheckList.
     */
    public ArrayList<CheckList> getChecklist() {
        return checklist;
    }

    /**
     * Imposta la lista delle sotto-attività.
     *
     * @param checklist La nuova lista checklist.
     */
    public void setChecklist(ArrayList<CheckList> checklist) {
        this.checklist = checklist;
    }

    /**
     * Aggiunge una singola attività alla checklist esistente.
     *
     * @param attivita L'attività da aggiungere.
     */
    public void addCheckListAttivita(CheckList attivita) {
        if (this.checklist == null) {
            this.checklist = new ArrayList<>();
        }
        this.checklist.add(attivita);
    }

    /**
     * Verifica lo stato della checklist interna.
     * Se tutte le voci della checklist sono contrassegnate come completate,
     * imposta automaticamente lo stato del ToDo principale come completato.
     */
    public void verificaECompletaToDo() {
        if (this.checklist == null || this.checklist.isEmpty()) {
            return;
        }
        boolean tutteCompletate = this.checklist.stream()
                .allMatch(CheckList::getStato);

        if (tutteCompletate && !this.completato) {
            this.setCompletato(true);
        }
    }

    /**
     * Restituisce una rappresentazione stringa dell'oggetto, corrispondente al titolo.
     * Utile per debug o visualizzazione in liste semplici.
     *
     * @return Il titolo del ToDo.
     */
    @Override
    public String toString() {
        return getTitolo();
    }
}