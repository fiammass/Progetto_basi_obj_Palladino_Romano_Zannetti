package dao;

import model.Utente;

/**
 * Interfaccia DAO che definisce i metodi per la gestione degli Utenti.
 * Le operazioni di persistenza principali che possono essere eseguite su un Utente,
 * indipoendentemente dal database utilizzato.
 */


public interface UtenteDao {

    /**
     * Registra un nuovo utente all interno del database.
     * @param utente
     */

    void salvautente(Utente utente);

    /**
     * Verifica se un certo utente è stato registrato nel database.
     * @param username
     * @param password
     * @return 1 se esite 0 se non esiste.
     */

    boolean VerificaLogin(String username,String password);

    /**
     * Restituisce un untente contenuto nel database in base al suo login aggiungendolo al model.
     * @param username
     * @return utente
     */

    Utente getUtentebyUsername (String username);

}