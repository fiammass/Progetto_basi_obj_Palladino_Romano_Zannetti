package implementazioni.postgres.dao;

import dao.ChecklistDao;
import database.ConnessioneDatabase;
import model.CheckList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CheckListImplementazione implements ChecklistDao {

    @Override
    public void salvaCheckListAttivita(CheckList attivita, int idToDo) {
        // CORREZIONE: Usiamo RETURNING per farci dare l'ID dal database
        String sql = "INSERT INTO checklist (nome, stato, idtodo) VALUES (?, ?, ?) RETURNING idchecklist";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, attivita.getNome());
            stmt.setBoolean(2, attivita.getStato());
            stmt.setInt(3, idToDo);

            // Usiamo executeQuery perché ci aspettiamo un risultato (l'ID)
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Recuperiamo l'ID (colonna 1)
                    int idGenerato = rs.getInt(1);
                    attivita.setIdChecklist(idGenerato);
                    attivita.setIdToDo(idToDo);
                    System.out.println("DEBUG: Checklist salvata. ID assegnato: " + idGenerato);
                }
            }

        } catch (SQLException e) {
            System.err.println("Errore salvataggio CheckList per ToDo ID: " + idToDo);
            e.printStackTrace();
        }
    }

    @Override
    public List<CheckList> getCheckListByToDoId(int idToDo) {
        List<CheckList> lista = new ArrayList<>();
        // Verifica se nel tuo DB la colonna si chiama idchecklist o id_checklist
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