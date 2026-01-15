package dao;

import model.Bacheca;
import java.util.List;


/**
 * Interfaccia DAO per la gestione della Bacheca.
 * Definisce le opersazione persistenti da fare su una Bacheca,
 * indipendentemente dal DB
 */

public interface BachecaDao {

    /**
     * Aggiorna la Bacheca quando vengono modificato o il titolo o la descrizione
     * @param bacheca
     * @param idBacheca
     */

    void updateBacheca (Bacheca bacheca, int idBacheca);

    /**
     * Restituisce le Bacheche di un Utente dal suo login (username)
     * @param login
     * @return
     */

    List<Bacheca> getBachecaByUtente(String login);


    void creaBacheca(String titolo, String descrizione, String proprietario, int numero);

}
