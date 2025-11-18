package implementazioni.postgres.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Utente;
import dao.UtenteDao;
import database.ConnessioneDatabase;


public class UtenteImplementazione implements UtenteDao {


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
