package implementazioni.postgres.dao;

import dao.ToDoDao;
import database.ConnessioneDatabase;
import model.Bacheca;
import model.ToDo;
import model.Utente;

import javax.imageio.ImageIO;
import java.io.File;
import java.sql.*;
import java.awt.*;

public class ToDoImplementazione implements ToDoDao {

    @Override
    public void salvaToDo(ToDo todo, int idBacheca, Utente autore) {
        // Schema corretto secondo foto (idba è presente)
        String sql = "INSERT INTO todo (titolo, completato, url, immagine, descrizione, data_scadenza, colore, autore, idba) " +
                "VALUES (?,?,?,?,?,?,?,?,?)";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmnt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmnt.setString(1, todo.getTitolo());
            stmnt.setBoolean(2, todo.getCompletato());
            stmnt.setString(3, todo.getUrl());
            stmnt.setString(4, todo.getImaginepath());
            stmnt.setString(5, todo.getDescrizione());
            stmnt.setDate(6, Date.valueOf(todo.getDatescadenza()));
            stmnt.setString(7, colorToString(todo.getColor()));
            stmnt.setString(8, autore.getlogin());
            stmnt.setInt(9, idBacheca); // SetInt è più sicuro di setString per interi

            stmnt.executeUpdate();

            try (ResultSet generatedKeys = stmnt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int idGenerato = generatedKeys.getInt(1); // idtodo è la prima colonna generata
                    todo.setIdToDo(idGenerato);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void popolabacheche(int idBacheca, Bacheca bacheca, Utente autore) {
        String sql = "SELECT idtodo, titolo, completato, url, immagine, descrizione, data_scadenza, colore, autore, idba FROM todo WHERE idba = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmnt = conn.prepareStatement(sql)) {

            stmnt.setInt(1, idBacheca);

            try (ResultSet rs = stmnt.executeQuery()) {
                while (rs.next()) {
                    String imagepath = rs.getString("immagine");
                    Image image = null;
                    try {
                        if (imagepath != null && !imagepath.isBlank()) {
                            File file = new File(imagepath);
                            if (file.exists()) {
                                image = ImageIO.read(file);
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("Errore caricamento immagine: " + e.getMessage());
                    }

                    String autoreLogin = rs.getString("autore");
                    Utente autoreTodo = new Utente(autoreLogin, "");

                    ToDo t = new ToDo(
                            rs.getString("titolo"),
                            rs.getDate("data_scadenza").toLocalDate(),
                            rs.getString("url"),
                            rs.getString("descrizione"),
                            image,
                            rs.getString("immagine"),
                            stringToColor(rs.getString("colore")),
                            autoreTodo,
                            bacheca,
                            rs.getBoolean("completato")
                    );

                    t.setIdToDo(rs.getInt("idtodo"));

                    boolean esiste = bacheca.getTodos().stream()
                            .anyMatch(existing -> existing.getIdToDo() == t.getIdToDo());

                    if (!esiste) {
                        bacheca.addToDo(t);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateToDo(ToDo todo, int idTodo) {
        String sql = "UPDATE todo SET titolo = ?, completato = ?, immagine = ?, descrizione = ?, data_scadenza = ?, colore = ? WHERE idtodo = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, todo.getTitolo());
            stmt.setBoolean(2, todo.getCompletato());
            stmt.setString(3, todo.getImaginepath());
            stmt.setString(4, todo.getDescrizione());
            stmt.setDate(5, Date.valueOf(todo.getDatescadenza()));
            stmt.setString(6, colorToString(todo.getColor()));
            stmt.setInt(7, idTodo);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cambiabacheca(ToDo ToDo, int idBacheca, int idToDo) {
        String sql = "UPDATE todo SET idba = ? WHERE idtodo = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idBacheca);
            stmt.setInt(2, idToDo);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void svuotabahcea(int idBacheca) {
        String sql = "DELETE FROM todo WHERE idba = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idBacheca);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eliminaToDo(ToDo ToDo, int idToDo) {
        String sql = "DELETE FROM todo WHERE idtodo = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idToDo);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getautore(int idToDo) {
        String autore = null;
        String sql = "SELECT autore FROM todo WHERE idtodo = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idToDo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    autore = rs.getString("autore");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return autore;
    }

    @Override
    public void popolaToDocondivisi(Utente utentecorrente, Bacheca bacheca, int numeroBachecaDest) {
        // CORRETTO:
        // 1. Aggiunti spazi prima di "FROM", "JOIN", "WHERE" (fondamentale!)
        // 2. "b.autore" -> "b.proprietario" (la bacheca ha il proprietario, non l'autore)
        String sql = "SELECT t.* , b.proprietario as numeroAutore " +
                "FROM todo t " +
                "JOIN condivisi c ON t.idtodo = c.idtodo " +
                "JOIN bacheca b ON t.idba = b.idbacheca " + // join su idbacheca
                "WHERE c.login_condiviso = ? AND b.numero = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, utentecorrente.getlogin());
            stmt.setInt(2, numeroBachecaDest);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ToDo t = new ToDo(
                        rs.getString("titolo"),
                        rs.getDate("data_scadenza").toLocalDate(),
                        rs.getString("url"),
                        rs.getString("descrizione"),
                        null,
                        rs.getString("immagine"),
                        stringToColor(rs.getString("colore")),
                        new Utente(rs.getString("autore"), ""),
                        bacheca, rs.getBoolean("completato")
                );
                t.setIdToDo(rs.getInt("idtodo"));

                if (bacheca.getTodos().stream().noneMatch(toDo -> toDo.getIdToDo() == t.getIdToDo()))
                    bacheca.addToDo(t);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String colorToString(Color color) {
        if (color == null) return "#808080";
        return String.format("#%06X", (0xFFFFFF & color.getRGB()));
    }

    public static Color stringToColor(String color) {
        try {
            return Color.decode(color);
        } catch (NumberFormatException e) {
            return Color.GRAY;
        }
    }
}