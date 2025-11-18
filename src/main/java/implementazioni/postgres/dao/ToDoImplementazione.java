package implementazioni.postgres.dao;

import dao.ToDoDao;
import database.ConnessioneDatabase;
import model.Bacheca;
import model.ToDo;
import model.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import javax.imageio.ImageIO;
import java.io.File;
import java.sql.*;
import java.awt.*;


/**
 * Implementazione DAO per la gestione dei ToDo in PostgreSQL.
 * Fornisce metodi per creare, modificare, eliminare e recuperare i ToDo
 * dal database, oltre a popolare le bacheche con i dati persistenti.
 * Si occupa anche di caricare i ToDo condivisi con un utente.
 */

public class ToDoImplementazione implements ToDoDao {


    /**
     * Inserisce all'interno del Database un todo
     *
     * @param todo il todo da inserire nel db
     * @param idBacheca l'id della bacheca in cui inserire il todo
     * @param autore l'autore del todo
     *
     */

    @Override
    public void salvaToDo(ToDo todo, int idBacheca, Utente autore) {

        String sql = "INSERT INTO todo (titolo , completato , url , immagine, descrizione, data_scadenza, colore, autore, idba)" +
                "VALUES ( ?,?,?,?,?,?,?,?,?)";

        try(Connection conn = ConnessioneDatabase.getConnection();
            PreparedStatement stmnt = conn.prepareStatement(sql , Statement.RETURN_GENERATED_KEYS)) {

            stmnt.setString(1 , todo.getTitolo());
            stmnt.setBoolean(2, todo.getCompletato());
            stmnt.setString(3, todo.getUrl());
            stmnt.setString(4, todo.getImaginepath());
            stmnt.setString(5 , todo.getDescrizione());
            stmnt.setDate(6, Date.valueOf(todo.getDatescadenza()));
            String colorestr = colorToString(todo.getColor());
            stmnt.setString(7, colorestr);
            stmnt.setString(8, autore.getlogin());
            stmnt.setString(9, String.valueOf(idBacheca));

            stmnt.executeUpdate();

            try(ResultSet generatedKeys =stmnt.getGeneratedKeys()) {
                if(generatedKeys.next()) {
                    int idGenerato = generatedKeys.getInt(1);
                    todo.setIdToDo(idGenerato);
                } else {
                    throw new SQLException("Creazione ToDo fallita, nessun Id ottenuto" );
                }
            }



        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void popolabacheche(int idBacheca, Bacheca bacheca, Utente autore) {

        // 1. Nella query SQL uso i nomi ESATTI dello screenshot
        // Nota: ho scritto 'aurore' perché nel tuo DB si chiama così
        String sql = "SELECT idtodo, titolo, completato, url, immagine, descrizione, data_scadenza, colore, autore, idba FROM todo WHERE idba = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmnt = conn.prepareStatement(sql)) {

            stmnt.setInt(1, idBacheca);

            try (ResultSet rs = stmnt.executeQuery()) {
                while (rs.next()) {

                    // --- GESTIONE IMMAGINE (con try-catch per sicurezza) ---
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
    public void updateToDo(ToDo ToDo, int idTodo) {

    }

    @Override
    public void cambiabacheca(ToDo ToDo, int idBacheca, int idToDo) {

    }

    @Override
    public void svuotabahcea(int idBacheca) {

    }

    @Override
    public void eliminaToDo(ToDo ToDo, int idToDo) {

    }

    @Override
    public String getautore(int idToDo) {
        return "";
    }

    @Override
    public void popolaToDocondivisi(Utente utentecorrente, Bacheca bacheca, int numeroBachecaDest) {

    }

    public static String colorToString(Color color) {
        if(color == null) return "#808080";
        return String.format("#%06X", (0xFFFFFF & color.getRGB()));
    }

    public static Color stringToColor(String color) {
        return Color.decode(color);
    }
}
