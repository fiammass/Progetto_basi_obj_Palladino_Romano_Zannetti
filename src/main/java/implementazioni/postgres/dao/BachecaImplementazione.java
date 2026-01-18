package implementazioni.postgres.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.BachecaDao;
import model.Bacheca;
import database.ConnessioneDatabase;

/**
 *Implementazione DAO per la gestione delle bacheche in postreSQL.
 * Fornisce metodi per aggiornare , recuperare ,creare una bachehca dal database
 */

public class BachecaImplementazione implements BachecaDao {


    /**
     * Metodo per l update della bacheha
     * @param bacheca
     * @param idBacheca
     */
    @Override
    public void updateBacheca(Bacheca bacheca, int idBacheca) {

        String sql = "UPDATE bacheca SET titolo = ?, descrizione = ? WHERE idbacheca = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, bacheca.getTitolo());
            stmt.setString(2, bacheca.getDescrizione());
            stmt.setInt(3, idBacheca);

            stmt.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo per ottenere la bahcea di un utente
     * @param login
     * @return
     */
    @Override
    public List<Bacheca> getBachecaByUtente(String login) {

        String sql = "SELECT idbacheca, titolo, descrizione, proprietario " +
                "FROM bacheca " +
                "WHERE proprietario = ? " +
                "ORDER BY idbacheca";

        List<Bacheca> bacheche = new ArrayList<>();

        try(Connection conn = ConnessioneDatabase.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                Bacheca b =  new Bacheca(
                        rs.getString("titolo"),
                        rs.getString("descrizione")
                );

                // PUNTO 3: Controlla questa riga
                // Errore comune: rs.getInt("id") -> DEVE ESSERE rs.getInt("idbacheca")
                b.setIdBa(rs.getInt("idbacheca"));

                bacheche.add(b);
            }

        } catch (SQLException e){
            e.printStackTrace();
        }

        return bacheche;
    }

    /**
     * Metodo per creare una bahchea da un utente
     * @param titolo
     * @param descrizione
     * @param proprietario
     * @param numero
     */

    @Override
    public void creaBacheca(String titolo, String descrizione, String proprietario, int numero) {
        String sql = "INSERT INTO bacheca (titolo, descrizione, proprietario, numero) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, titolo);
            stmt.setString(2, descrizione);
            stmt.setString(3, proprietario);
            stmt.setInt(4, numero);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}