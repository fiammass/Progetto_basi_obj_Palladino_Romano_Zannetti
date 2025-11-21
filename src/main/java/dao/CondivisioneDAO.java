package dao;

import model.Utente;
import java.util.List;

public interface CondivisioneDAO {

    /**
     * Aggiunge un record di condivisione tra un ToDo e un utente destinatario.
     * @param idToDo ID del ToDo da condividere.
     * @param loginDestinatario Login dell'utente che riceve la condivisione.
     */
    void aggiungiCondivisione(int idToDo, String loginDestinatario);

    /**
     * Rimuove un record di condivisione specifico.
     * @param idToDo ID del ToDo.
     * @param loginDestinatario Login dell'utente destinatario.
     */
    void eliminaCondivisionePerUtente(int idToDo, String loginDestinatario);

    /**
     * Recupera la lista di Utenti che condividono un dato ToDo.
     * @param idToDo ID del ToDo.
     * @return Lista di Utenti.
     */
    List<Utente> getUtentiCondivisi(int idToDo);

    /**
     * Elimina tutti i record di condivisione relativi ai ToDo contenuti in una specifica Bacheca.
     * Necessario prima di eliminare la bacheca.
     * @param idBacheca ID della Bacheca.
     */
    void eliminaCondivisioniDellaBacheca(int idBacheca);
}