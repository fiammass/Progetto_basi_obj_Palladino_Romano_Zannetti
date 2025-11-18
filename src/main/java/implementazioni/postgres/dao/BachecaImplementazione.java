package implementazioni.postgres.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.BachecaDao;
import model.Utente;
import model.Bacheca;
import dao.UtenteDao;
import database.ConnessioneDatabase;

/**
 * implementazione DAO per la gestione delle Bacheche nel DB in PostgreSQL,
 * fronisce metodi di per aggiornare e mostrare la lista delle bacheche di un Utente nel DB
 */

public class BachecaImplementazione  implements BachecaDao {


    /**
     *  Aggiorna al cambiare le titolo o della descrizione la Bacheca.
     * @param bacheca
     * @param idBacheca
     */

    @Override
    public void updateBacheca(Bacheca bacheca, int idBacheca) {

    String sql = "UPDATE bahceca SET titolo = ?, descrizione = ? WHERE id = ?";

    try(Connection conn = ConnessioneDatabase.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, bacheca.getTitolo());
        stmt.setString(2, bacheca.getDescrizione());
        stmt.setInt(3, idBacheca);

        stmt.executeQuery();

        } catch (Exception e){

        e.printStackTrace();
    }

    }

    /**
     * Metodo che visualizza le Bacheche di ogni Utente
     * @param login
     * @return lista delle bacheche di un Utente.
     */

    @Override
    public List<Bacheca> getBachecaByUtente(String login) {

        String sql = "SELECT id, titolo, descrizione, proprietario " +
                "FROM bacheca " +
                "WHERE proprietario = ? " +
                "ORDER BY id";

            List<Bacheca> bacheche = new ArrayList<>();

            try(Connection conn = ConnessioneDatabase.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, login);
                ResultSet rs = stmt.executeQuery();

                while(rs.next()) {
                    Bacheca b =  new Bacheca(
                            rs.getString("titolo"),
                            rs.getString("descrizione"),
                            );
                    b.setIdBa(rs.getInt("id"));
                    bacheche.add(b);

                }

            } catch (Exception e){
                e.printStackTrace();
            }



        return bacheche;
    }
}
