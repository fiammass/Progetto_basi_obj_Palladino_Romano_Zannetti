package dao;

import model.Utente;

public interface UtenteDao {

    void salvautente(Utente utente);

    boolean VerificaLogin(String username,String password);

    Utente getUtentebyUsername (String username);

}