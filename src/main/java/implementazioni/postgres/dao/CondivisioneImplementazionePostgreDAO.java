package implementazioni.postgres.dao;

import dao.CondivisioneDAO;
import dao.UtenteDao;
import database.ConnessioneDatabase;
import model.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Implementazione DAO per la gestione delle Condivisioni in PostgreSQL.
 * Fornisce metodi per aggiungere, eliminare per utente ,recuperare l ultente condiviso , eliminare per bahchea.
 */
public class CondivisioneImplementazionePostgreDAO implements CondivisioneDAO {

    // DIPENDENZA: UtenteDAO è necessario per caricare i dettagli degli utenti condivisi
    private UtenteDao utenteDAO = new UtenteImplementazione();

    /**
     * Metodo per aggiungere una condivisione di un Todo
     * @param idToDo ID del ToDo da condividere.
     * @param loginDestinatario Login dell'utente che riceve la condivisione.
     */
    @Override
    public void aggiungiCondivisione(int idToDo, String loginDestinatario) {
        String sql = "INSERT INTO condivisi (idtodo, login_condiviso) VALUES (?, ?)";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idToDo);
            stmt.setString(2, loginDestinatario);

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Errore nell'aggiungere la condivisione per " + loginDestinatario);
            e.printStackTrace();
        }
    }

    /**
     * Metodo per eliminare una condivisone specifica per un Utente
      * @param idToDo ID del ToDo.
     * @param loginDestinatario Login dell'utente destinatario.
     */
    @Override
    public void eliminaCondivisionePerUtente(int idToDo, String loginDestinatario) {
        String sql = "DELETE FROM condivisi WHERE idtodo = ? AND login_condiviso = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idToDo);
            stmt.setString(2, loginDestinatario);

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Errore nell'eliminare la condivisione per " + loginDestinatario);
            e.printStackTrace();
        }
    }

    /**
     * Metodo per recuperare gli utenteni condivisi da uno specifico Todo
      * @param idToDo ID del ToDo.
     * @return
     */
    @Override
    public List<Utente> getUtentiCondivisi(int idToDo) {
        List<Utente> utenti = new ArrayList<>();
        // Query per recuperare tutti i login dalla tabella condivisi
        String sql = "SELECT login_condiviso FROM condivisi WHERE idtodo = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idToDo);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String login = rs.getString("login_condiviso");
                    // Riusa il metodo di UtenteDAO per recuperare l'oggetto Utente completo
                    Utente utente = utenteDAO.getUtentebyUsername(login);
                    if (utente != null) {
                        utenti.add(utente);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore nel recupero degli utenti condivisi per ToDo ID: " + idToDo);
            e.printStackTrace();
        }
        return utenti;
    }

    /**
     * Metodo per eliminare la condivisione di una bahcheca
     * @param idBacheca ID della Bacheca.
     */
    public void eliminaCondivisioniDellaBacheca(int idBacheca) {
        // Query complessa che trova tutti i ToDo in una bacheca ed elimina i relativi record di condivisione
        String sql = "DELETE FROM condivisi c USING todo t WHERE c.idtodo = t.idtodo AND t.idba = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idBacheca);

            int righeEliminate = stmt.executeUpdate();
            System.out.println("Eliminate " + righeEliminate + " condivisioni per la bacheca " + idBacheca);

        } catch (SQLException e) {
            System.err.println("Errore nell'eliminare le condivisioni per la Bacheca ID: " + idBacheca);
            e.printStackTrace();
        }
    }
}