package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



/**
 * Classe di utilità per la gestione della connessione al database.
 * Fornisce un metodo per ottenere una connessione al DB
 */

public class ConnessioneDatabase {

    private Connection connection = null;
    private String nome = "postgres";
    private String password = "password";
    private String url = "jdbc:postgresql://localhost:5432/postgres";
    private String driver = "org.postgresql.Driver";

    /**
     * Costruttore classe connessione al database
     * */



    private ConnessioneDatabase() throws SQLException{

        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, nome, password);
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }

    }

    /**
     * Restituisce una connessione al DB
     * @return la connessione al database.
     */

    public static Connection getConnection() throws SQLException{

        ConnessioneDatabase connessioneDatabase = new ConnessioneDatabase();
        return connessioneDatabase.connection;
    }


}
