package dao;

import model.Utente;

/**
 * questa intefaccia serve per collegare il database alla classe model
 */


public interface UtenteDao {

    void salvautente(Utente utente);

    boolean VerificaLogin(String username,String password);

    Utente getUtentebyUsername (String username);

}