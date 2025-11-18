package model;

import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Rappresenta un'attività ToDo all'interno del sistema.
 * Un ToDo contiene titolo, descrizione, scadenza, eventuale immagine,
 * stato di completamento e il riferimento alla bacheca e all'autore.
 * I ToDo possono essere condivisi con altri utenti, mantenendo però
 * un'unica istanza originale nel database.
 */
public class ToDo {


    private Integer idToDo;
    private String titolo;
    private LocalDate datescadenza;
    private String url;
    private String descrizione;
    private Image image;
    private Utente autore;
    private ArrayList<Utente> condivisioni;
    private Color color;
    private Boolean completato = false;
    private Bacheca bacheca;
    private String imaginepath;

    /**
     * Costruttore della classe ToDo
     *
     * @param titolo Il titolo del ToDo
     * @param datescadenza La data di scadenza
     * @param url Il link associato
     * @param descrizione La descrizione
     * @param image L'oggetto immagine caricato
     * @param imaginepath Il percorso del file immagine
     * @param color Il colore del post-it
     * @param autore L'utente che ha creato il todo
     * @param bacheca La bacheca di appartenenza
     * @param completato Lo stato del task
     */
    public ToDo(String titolo, LocalDate datescadenza, String url, String descrizione,
                Image image, String imaginepath, Color color,
                Utente autore, Bacheca bacheca, Boolean completato) {

        this.titolo = titolo;
        this.datescadenza = datescadenza;
        this.url = url;
        this.descrizione = descrizione;
        this.image = image;
        this.color = color;
        this.autore = autore;
        this.condivisioni = new ArrayList<>();
        this.bacheca = bacheca;
        this.imaginepath = imaginepath;
        this.completato = completato;

        // Aggiunge se stesso alla lista della bacheca (come nel tuo esempio)
        if (bacheca != null) {
            bacheca.getTodos().add(this);
        }
        setImaginepath(imaginepath);
    }

    /**
     * Restituisce l'id
     *
     * @return l'id
     */
    public Integer getIdToDo() {
        return idToDo;
    }

    /**
     * Restituisce il titolo
     *
     * @return il titolo
     */
    public String getTitolo() {
        return titolo;
    }

    /**
     * Restituisce la scadenza del todo
     *
     * @return la scadenza
     */
    public LocalDate getDatescadenza() {
        return datescadenza;
    }

    /**
     * Restituisce il link
     *
     * @return il link
     */
    public String getUrl() {
        return url;
    }

    /**
     * Restituisce la descrizione
     *
     * @return la descrizione
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * Restituisce l'immagine
     *
     * @return l'immagine
     */
    public Image getImage() {
        return image;
    }

    /**
     * Restituisce il colore
     *
     * @return il colore
     */
    public Color getColor() {
        return color;
    }

    /**
     * Restituisce lo stato
     *
     * @return lo stato
     */
    public Boolean getCompletato() {
        return completato;
    }

    /**
     * Restituisce l'elenco degli utenti con cui è condiviso il todo
     *
     * @return la lista degli utenti
     */
    public ArrayList<Utente> getCondivisioni() {
        return condivisioni;
    }

    /**
     * Restituisce l'autore del todo
     *
     * @return l'autore
     */
    public Utente getAutore() {
        return autore;
    }

    /**
     * Restituisce la bacheca dove è contenuto il todo
     *
     * @return la bacheca
     */
    public Bacheca getBacheca() {
        return bacheca;
    }

    /**
     * Restituisce il path dell'immagine collegata al todo
     *
     * @return il path dell'immagine
     */
    public String getImaginepath() {
        return imaginepath;
    }

    // --- SETTER ---

    /**
     * Imposta l'id del todo
     *
     * @param idToDo il nuovo id
     */
    public void setIdToDo(Integer idToDo) {
        this.idToDo = idToDo;
    }

    /**
     * Imposta il titolo del todo
     *
     * @param titolo il nuovo titolo
     */
    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    /**
     * Imposta la scadenza del todo
     *
     * @param datescadenza la nuova scadenza
     */
    public void setDatescadenza(LocalDate datescadenza) {
        this.datescadenza = datescadenza;
    }

    /**
     * Imposta il link legato al todo
     *
     * @param url il nuovo link
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Imposta la descrizione del todo
     *
     * @param descrizione la nuova descrizione
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * Imposta l'immagine del todo
     *
     * @param image la nuova immagine
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * Imposta il path dell'immagine del todo
     *
     * @param imaginepath il nuovo path
     */
    public void setImaginepath(String imaginepath) {
        this.imaginepath = imaginepath;
    }

    /**
     * Imposta il colore del todo
     *
     * @param color il nuovo colore
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Imposta la bacheca del todo
     *
     * @param bacheca la nuova bacheca
     */
    public void setBacheca(Bacheca bacheca) {
        this.bacheca = bacheca;
    }

    /**
     * Imposta lo stato del todo
     *
     * @param completato il nuovo stato
     */
    public void setCompletato(Boolean completato) {
        this.completato = completato;
    }

    /**
     * Imposta l'autore del todo
     *
     * @param autore il nuovo autore
     */
    public void setAutore(Utente autore) {
        this.autore = autore;
    }

    /**
     * Imposta la lista delle condivisioni
     * @param condivisioni la nuova lista
     */
    public void setCondivisioni(ArrayList<Utente> condivisioni) {
        this.condivisioni = condivisioni;
    }

    @Override
    public String toString() {
        return getTitolo();
    }
}