package implementazioni.postgres.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Utente;
import dao.UtenteDao;
import database.ConnessioneDatabase;

/**
 * implementazione DAO per la gestione degli utenti nel DB in PostgreSQL,
 * fronisce metodi di per aggiungere , recuperare e confrontare gli utenti dal DB
 */

public class UtenteImplementazione implements UtenteDao {


    /**
     * registra un nuovo utente all' interno del DB
     * @param utente
     */

    @Override
    public void salvautente(Utente utente) {

        String sql = "INSERT INTO login_utenti (login, password) VALUES (? , ?)  ";

        try (Connection conn = ConnessioneDatabase.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, utente.getlogin());
            stmt.setString(2, utente.getPassword());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * verifica che un utente si è registrato nel DB trovando una corrispondenza
     * @param username
     * @param password
     * @return 1 se esiste 0 se non esiste
     */

    @Override
    public boolean VerificaLogin(String username, String password) {

        String sql = "SELECT COUNT(*) FROM login_utenti WHERE login = ? AND password = ?";

        try(Connection conn = ConnessioneDatabase.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1,username);
            stmt.setString(2,password);

            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    int count = rs.getInt( 1);
                    return count > 0;
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Restituisce un utente contenuto nel database con un certo username, aggiungendolo al model
     * @param username
     * @return
     */

    public Utente getUtentebyUsername(String username) {
        Utente utente = null;

        String sqlUtente = "SELECT login , password FROM login_utenti WHERE login = ?";

        try(Connection conn = ConnessioneDatabase.getConnection();
            PreparedStatement stmtUtente = conn.prepareStatement(sqlUtente)) {

            stmtUtente.setString(1,username);

            try(ResultSet rs = stmtUtente.executeQuery()){
                if (rs.next()) {
                    utente = new Utente(rs.getString("login"), rs.getString("password"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return utente;

    }
}
