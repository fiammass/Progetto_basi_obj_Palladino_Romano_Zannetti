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
 * Implementazione DAO per la gestione delle Condivisioni su database PostgreSQL.
 * <p>
 * Questa classe gestisce la tabella di relazione 'condivisi' (molti-a-molti),
 * che collega i ToDo agli utenti che hanno il permesso di visualizzarli.
 */
public class CondivisioneImplementazionePostgreDAO implements CondivisioneDAO {

    private UtenteDao utenteDAO = new UtenteImplementazione();

    /**
     * Aggiunge un record di condivisione nel database.
     * Se la condivisione esiste già (chiave duplicata), l'eccezione viene gestita
     * stampando un messaggio di log senza interrompere l'esecuzione.
     *
     * @param idToDo            L'ID del ToDo da condividere.
     * @param loginDestinatario Lo username dell'utente con cui condividere il ToDo.
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
            System.err.println("Info: Condivisione già esistente o errore SQL: " + e.getMessage());
        }
    }

    /**
     * Rimuove una specifica condivisione dal database.
     * L'utente indicato non vedrà più il ToDo nella propria bacheca.
     *
     * @param idToDo            L'ID del ToDo.
     * @param loginDestinatario Lo username dell'utente da rimuovere.
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
            e.printStackTrace();
        }
    }

    /**
     * Recupera la lista completa degli utenti con cui un determinato ToDo è condiviso.
     * Utilizza {@link UtenteDao} per recuperare i dettagli completi di ogni utente trovato.
     *
     * @param idToDo L'ID del ToDo di cui cercare le condivisioni.
     * @return Una lista di oggetti {@link Utente}.
     */
    @Override
    public List<Utente> getUtentiCondivisi(int idToDo) {
        List<Utente> utenti = new ArrayList<>();
        String sql = "SELECT login_condiviso FROM condivisi WHERE idtodo = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idToDo);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String login = rs.getString("login_condiviso");
                    Utente utente = utenteDAO.getUtentebyUsername(login);
                    if (utente != null) {
                        utenti.add(utente);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utenti;
    }

    /**
     * Elimina tutte le condivisioni associate a tutti i ToDo di una specifica bacheca.
     * Utile per la pulizia dei dati quando una bacheca viene eliminata.
     *
     * @param idBacheca L'ID della bacheca.
     */
    @Override
    public void eliminaCondivisioniDellaBacheca(int idBacheca) {
        String sql = "DELETE FROM condivisi c USING todo t WHERE c.idtodo = t.idtodo AND t.idba = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idBacheca);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}