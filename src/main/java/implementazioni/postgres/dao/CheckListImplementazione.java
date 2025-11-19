package implementazioni.postgres.dao;

import dao.ChecklistDao;
import database.ConnessioneDatabase;
import model.CheckList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione DAO per la gestione delle CheckList (sottoattività) in PostgreSQL.
 * Assumiamo che la tabella DB sia: checklist(idchecklist, nome, stato, idtodo)
 */
public class CheckListImplementazione implements ChecklistDao {


    /**
     * Metodo che salva la checklist.
     * @param attivita L'attività CheckList da salvare.
     * @param idToDo L'ID del ToDo a cui è associata l'attività.
     */

    @Override
    public void salvaCheckListAttivita(CheckList attivita, int idToDo) {

        String sql = "INSERT INTO checklist (nome, stato, idtodo) VALUES (?, ?, ?)";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, attivita.getNome());
            stmt.setBoolean(2, attivita.getStato());
            stmt.setInt(3, idToDo);

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    attivita.setIdChecklist(generatedKeys.getInt(1));
                    attivita.setIdToDo(idToDo);
                }
            }

        } catch (SQLException e) {
            System.err.println("Errore salvataggio CheckList per ToDo ID: " + idToDo);
            e.printStackTrace();
        }
    }

    /**
     * Metodo che restituisce una checkilst all interno del todo.
     * @param idToDo L'ID del ToDo.
     * @return
     */

    @Override
    public List<CheckList> getCheckListByToDoId(int idToDo) {
        List<CheckList> lista = new ArrayList<>();

        String sql = "SELECT idchecklist, nome, stato FROM checklist WHERE idtodo = ?";

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
     * Metodo che aggiorna la checklist in basse al suo stato.
     * @param idCheckList L'ID dell'attività CheckList.
     * @param completato Il nuovo stato.
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
     * metodo che elimina una checklist una colta completata.
     * @param idCheckList L'ID dell'attività CheckList da eliminare.
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