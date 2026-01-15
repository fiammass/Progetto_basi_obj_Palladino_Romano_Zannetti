package implementazioni.postgres.dao;

import dao.ChecklistDao;
import database.ConnessioneDatabase;
import model.CheckList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione DAO per la gestione delle CheckList (sottoattività) in PostgreSQL.
 * <p>
 * Questa classe gestisce le operazioni CRUD sulla tabella 'checklist' del database,
 * permettendo di salvare, recuperare, aggiornare ed eliminare le singole voci
 * associate a un ToDo.
 */
public class CheckListImplementazione implements ChecklistDao {

    /**
     * Salva una nuova attività della checklist nel database.
     * Utilizza la clausola SQL {@code RETURNING} per recuperare immediatamente
     * l'ID generato dal database e assegnarlo all'oggetto Java.
     *
     * @param attivita L'oggetto CheckList da salvare (contenente nome e stato).
     * @param idToDo   L'ID del ToDo a cui questa attività appartiene.
     */
    @Override
    public void salvaCheckListAttivita(CheckList attivita, int idToDo) {
        String sql = "INSERT INTO checklist (nome, stato, idtodo) VALUES (?, ?, ?) RETURNING idchecklist";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, attivita.getNome());
            stmt.setBoolean(2, attivita.getStato());
            stmt.setInt(3, idToDo);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int idGenerato = rs.getInt(1);
                    attivita.setIdChecklist(idGenerato);
                    attivita.setIdToDo(idToDo);
                }
            }

        } catch (SQLException e) {
            System.err.println("Errore salvataggio CheckList per ToDo ID: " + idToDo);
            e.printStackTrace();
        }
    }

    /**
     * Recupera tutte le attività della checklist associate a uno specifico ToDo.
     * Le attività vengono restituite ordinate per ID crescente (ordine di inserimento).
     *
     * @param idToDo L'ID del ToDo di cui recuperare la checklist.
     * @return Una lista di oggetti {@link CheckList}.
     */
    @Override
    public List<CheckList> getCheckListByToDoId(int idToDo) {
        List<CheckList> lista = new ArrayList<>();
        String sql = "SELECT idchecklist, nome, stato FROM checklist WHERE idtodo = ? ORDER BY idchecklist";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idToDo);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    CheckList cl = new CheckList(
                            rs.getString("nome"),
                            rs.getBoolean("stato")
                    );
                    cl.setIdChecklist(rs.getInt("idchecklist"));
                    cl.setIdToDo(idToDo);
                    lista.add(cl);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Aggiorna lo stato di completamento di una singola voce della checklist.
     *
     * @param idCheckList L'ID dell'attività da aggiornare.
     * @param completato  Il nuovo stato (true = completato, false = da fare).
     */
    @Override
    public void updateStatoCheckList(int idCheckList, boolean completato) {
        String sql = "UPDATE checklist SET stato = ? WHERE idchecklist = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, completato);
            stmt.setInt(2, idCheckList);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Errore aggiornamento stato CheckList ID: " + idCheckList);
            e.printStackTrace();
        }
    }

    /**
     * Elimina definitivamente una voce della checklist dal database.
     *
     * @param idCheckList L'ID dell'attività da eliminare.
     */
    @Override
    public void eliminaCheckListAttivita(int idCheckList) {
        String sql = "DELETE FROM checklist WHERE idchecklist = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCheckList);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Errore eliminazione CheckList ID: " + idCheckList);
            e.printStackTrace();
        }
    }
}