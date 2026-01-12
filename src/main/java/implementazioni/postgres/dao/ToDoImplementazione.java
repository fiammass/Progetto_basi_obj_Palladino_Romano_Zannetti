package implementazioni.postgres.dao;

import dao.ToDoDao;
import database.ConnessioneDatabase;
import model.Bacheca;
import model.ToDo;
import model.Utente;

import java.sql.*;
import java.time.LocalDate;

public class ToDoImplementazione implements ToDoDao {

    @Override
    public void salvaToDo(ToDo todo, int idBacheca, Utente utente) {
        String sql = "INSERT INTO todo (titolo, descrizione, data_scadenza, completato, colore, idba, autore) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING idtodo";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, todo.getTitolo());
            stmt.setString(2, todo.getDescrizione());

            if (todo.getDatescadenza() != null) {
                stmt.setDate(3, Date.valueOf(todo.getDatescadenza()));
            } else {
                stmt.setNull(3, Types.DATE);
            }

            stmt.setBoolean(4, Boolean.TRUE.equals(todo.getCompletato()));

            if (todo.getColor() != null) {
                stmt.setInt(5, todo.getColor().getRGB());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }

            stmt.setInt(6, idBacheca);
            stmt.setString(7, utente.getlogin());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int idGenerato = rs.getInt(1);
                    todo.setIdToDo(idGenerato);
                    System.out.println("DEBUG: ToDo salvato correttamente con ID: " + idGenerato);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("ERRORE SQL INSERT: " + e.getMessage());
        }
    }

    @Override
    public void updateToDo(ToDo todo, int idTodo) {
        String sql = "UPDATE todo SET titolo = ?, descrizione = ?, data_scadenza = ?, completato = ?, colore = ? WHERE idtodo = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, todo.getTitolo());
            stmt.setString(2, todo.getDescrizione());

            if (todo.getDatescadenza() != null) {
                stmt.setDate(3, Date.valueOf(todo.getDatescadenza()));
            } else {
                stmt.setNull(3, Types.DATE);
            }

            stmt.setBoolean(4, Boolean.TRUE.equals(todo.getCompletato()));

            if (todo.getColor() != null) {
                stmt.setInt(5, todo.getColor().getRGB());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }

            stmt.setInt(6, idTodo);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void popolabacheche(int idBacheca, Bacheca bacheca, Utente utente) {
        String sql = "SELECT * FROM todo WHERE idba = ? ORDER BY idtodo";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idBacheca);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ToDo t = creaToDoDaResultSet(rs, utente, bacheca);
                    if (bacheca.getTodos() != null && !bacheca.getTodos().contains(t)) {
                        bacheca.addTodo(t);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // --- METODO ELIMINA AGGIORNATO ---
    @Override
    public void eliminaToDo(ToDo ToDo, int idToDo) {
        // CORREZIONE: usiamo "WHERE idtodo = ?"
        String sql = "DELETE FROM todo WHERE idtodo = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idToDo);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("DEBUG: ToDo eliminato con successo (ID: " + idToDo + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // ---------------------------------

    private ToDo creaToDoDaResultSet(ResultSet rs, Utente utente, Bacheca bacheca) throws SQLException {
        int id = rs.getInt("idtodo");
        String titolo = rs.getString("titolo");
        String desc = rs.getString("descrizione");
        Date dataSql = rs.getDate("data_scadenza");
        LocalDate data = (dataSql != null) ? dataSql.toLocalDate() : null;
        boolean completato = rs.getBoolean("completato");
        int rgb = rs.getInt("colore");
        java.awt.Color colore = (rs.wasNull()) ? null : new java.awt.Color(rgb);

        ToDo t = new ToDo(titolo, data, null, desc, null, null, colore, utente, bacheca, completato);
        t.setIdToDo(id);
        return t;
    }

    @Override public void popolaToDocondivisi(Utente u, Bacheca b, int i) {}
    @Override public void cambiabacheca(ToDo t, int idB, int idT) {}
    @Override public void svuotabahcea(int idB) {}
    @Override public String getautore(int idT) { return null; }
}