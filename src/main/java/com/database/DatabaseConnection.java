package com.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestisce la connessione al com.database PostgreSQL con pattern Singleton.
 * Include anche metodi per transazioni.
 */
public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/todo_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "password";
    private static Connection connection;

    // Blocca l'istanziamento esterno
    private DatabaseConnection() {}

    /**
     * Ottiene la connessione (creandola se non esiste)
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                connection.setAutoCommit(false); // Gestione manuale transazioni
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver PostgreSQL non trovato");
            }
        }
        return connection;
    }

    /**
     * Chiude la connessione esistente
     */
    public static void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    /**
     * Esegue il commit della transazione corrente
     */
    public static void commit() throws SQLException {
        if (connection != null && !connection.getAutoCommit()) {
            connection.commit();
        }
    }

    /**
     * Esegue il rollback della transazione corrente
     */
    public static void rollback() throws SQLException {
        if (connection != null && !connection.getAutoCommit()) {
            connection.rollback();
        }
    }
}