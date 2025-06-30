package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Contiene gli script SQL per inizializzare il database.
 * Viene eseguito all'avvio dell'applicazione.
 */
public class DatabaseSchema {

    public static void createTables() throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // Script SQL per creare tutte le tabelle
            String[] createTables = {
                    """
                CREATE TABLE IF NOT EXISTS users (
                    id SERIAL PRIMARY KEY,
                    username VARCHAR(50) UNIQUE NOT NULL,
                    password VARCHAR(100) NOT NULL
                )
                """,
                    """
                CREATE TABLE IF NOT EXISTS boards (
                    id SERIAL PRIMARY KEY,
                    title VARCHAR(50) NOT NULL,
                    description TEXT,
                    type VARCHAR(20) CHECK (type IN ('UNIVERSITA', 'LAVORO', 'TEMPO_LIBERO'))
                """,
                    """
                CREATE TABLE IF NOT EXISTS todos (
                    id SERIAL PRIMARY KEY,
                    title VARCHAR(100),
                    description TEXT,
                    due_date TIMESTAMP,
                    image_url VARCHAR(255),
                    color VARCHAR(20),
                    completed BOOLEAN DEFAULT false,
                    position INTEGER NOT NULL,
                    board_id INTEGER REFERENCES boards(id),
                    user_id INTEGER REFERENCES users(id)
                """,
                    """
                CREATE TABLE IF NOT EXISTS checklist_items (
                    id SERIAL PRIMARY KEY,
                    description TEXT NOT NULL,
                    completed BOOLEAN DEFAULT false,
                    todo_id INTEGER REFERENCES todos(id) ON DELETE CASCADE
                )
                """,
                    """
                CREATE TABLE IF NOT EXISTS shared_todos (
                    todo_id INTEGER REFERENCES todos(id) ON DELETE CASCADE,
                    user_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
                    PRIMARY KEY (todo_id, user_id)
                )
                """
            };

            // Esegue tutti gli script
            for (String sql : createTables) {
                stmt.executeUpdate(sql);
            }

            // Inserisce le 3 bacheche predefinite se non esistono
            stmt.executeUpdate("""
                INSERT INTO boards (title, type) 
                VALUES ('Università', 'UNIVERSITA'),
                       ('Lavoro', 'LAVORO'),
                       ('Tempo Libero', 'TEMPO_LIBERO')
                ON CONFLICT (type) DO NOTHING
                """);

            DatabaseConnection.commit();
        } catch (SQLException e) {
            DatabaseConnection.rollback();
            throw e;
        }
    }
}