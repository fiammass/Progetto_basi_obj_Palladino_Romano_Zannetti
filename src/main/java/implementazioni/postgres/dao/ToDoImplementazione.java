package implementazioni.postgres.dao;

import dao.ToDoDao;
import database.ConnessioneDatabase;
import model.Bacheca;
import model.ToDo;
import model.Utente;

import java.sql.*;
import java.time.LocalDate;

/**
 * Implementazione DAO per la gestione dei ToDo su database PostgreSQL.
 * <p>
 * Questa classe gestisce tutte le operazioni CRUD (Create, Read, Update, Delete)
 * relative ai ToDo, inclusi il salvataggio, la modifica, l'eliminazione e il recupero
 * sia dei ToDo personali che di quelli condivisi.
 */
public class ToDoImplementazione implements ToDoDao {

    /**
     * Salva un nuovo ToDo nel database e recupera l'ID generato.
     * Utilizza la clausola SQL {@code RETURNING idtodo} per ottenere l'ID autoincrementale
     * e assegnarlo immediatamente all'oggetto Java passato come parametro.
     *
     * @param todo      L'oggetto ToDo da salvare.
     * @param idBacheca L'ID della bacheca in cui inserire il ToDo.
     * @param utente    L'autore del ToDo.
     */
    @Override
    public void salvaToDo(ToDo todo, int idBacheca, Utente utente) {
        String sql = "INSERT INTO todo (titolo, descrizione, data_scadenza, completato, colore, idba, autore) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING idtodo";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, todo.getTitolo());
            stmt.setString(2, todo.getDescrizione());
            if (todo.getDatescadenza() != null) stmt.setDate(3, Date.valueOf(todo.getDatescadenza()));
            else stmt.setNull(3, Types.DATE);
            stmt.setBoolean(4, Boolean.TRUE.equals(todo.getCompletato()));
            if (todo.getColor() != null) stmt.setInt(5, todo.getColor().getRGB());
            else stmt.setNull(5, Types.INTEGER);
            stmt.setInt(6, idBacheca);
            stmt.setString(7, utente.getlogin());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    todo.setIdToDo(rs.getInt(1));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    /**
     * Aggiorna i dati di un ToDo esistente nel database.
     * Modifica titolo, descrizione, scadenza, stato di completamento e colore.
     *
     * @param todo   L'oggetto ToDo contenente i dati aggiornati.
     * @param idTodo L'ID del ToDo da aggiornare.
     */
    @Override
    public void updateToDo(ToDo todo, int idTodo) {
        String sql = "UPDATE todo SET titolo = ?, descrizione = ?, data_scadenza = ?, completato = ?, colore = ? WHERE idtodo = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, todo.getTitolo());
            stmt.setString(2, todo.getDescrizione());
            if (todo.getDatescadenza() != null) stmt.setDate(3, Date.valueOf(todo.getDatescadenza()));
            else stmt.setNull(3, Types.DATE);
            stmt.setBoolean(4, Boolean.TRUE.equals(todo.getCompletato()));
            if (todo.getColor() != null) stmt.setInt(5, todo.getColor().getRGB());
            else stmt.setNull(5, Types.INTEGER);
            stmt.setInt(6, idTodo);
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    /**
     * Recupera tutti i ToDo appartenenti a una specifica bacheca e li aggiunge al modello.
     *
     * @param idBacheca L'ID della bacheca da popolare.
     * @param bacheca   L'oggetto Bacheca in cui aggiungere i ToDo trovati.
     * @param utente    L'utente proprietario della bacheca.
     */
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
        } catch (SQLException e) { e.printStackTrace(); }
    }

    /**
     * Recupera i ToDo che sono stati condivisi con l'utente corrente da altri utenti.
     * Filtra i ToDo in base al tipo di bacheca (numero) per mantenere la coerenza
     * (es. un ToDo condiviso da una bacheca "Lavoro" apparirà nella bacheca "Lavoro" del destinatario).
     *
     * @param utenteCorrente      L'utente che sta visualizzando la bacheca.
     * @param bachecaDestinazione La bacheca in cui aggiungere i ToDo condivisi.
     * @param numeroBacheca       Il tipo di bacheca (1=Uni, 2=Lavoro, 3=Svago).
     */
    @Override
    public void popolaToDocondivisi(Utente utenteCorrente, Bacheca bachecaDestinazione, int numeroBacheca) {
        String sql = "SELECT t.* FROM todo t " +
                "JOIN condivisi c ON t.idtodo = c.idtodo " +
                "JOIN bacheca b_origin ON t.idba = b_origin.idbacheca " +
                "WHERE c.login_condiviso = ? AND b_origin.numero = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, utenteCorrente.getlogin());
            stmt.setInt(2, numeroBacheca);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ToDo t = creaToDoDaResultSet(rs, utenteCorrente, bachecaDestinazione);

                    String veroAutore = rs.getString("autore");
                    t.setAutore(new Utente(veroAutore, ""));

                    if (!bachecaDestinazione.getTodos().contains(t)) {
                        bachecaDestinazione.addTodo(t);
                    }
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    /**
     * Elimina un ToDo dal database.
     *
     * @param t   L'oggetto ToDo da eliminare (non utilizzato direttamente nella query).
     * @param idT L'ID del ToDo da eliminare.
     */
    @Override
    public void eliminaToDo(ToDo t, int idT) {
        String sql = "DELETE FROM todo WHERE idtodo = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idT);
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    /**
     * Metodo di utilità per convertire una riga del ResultSet in un oggetto ToDo.
     * Gestisce la conversione delle date e dei colori (gestione dei null).
     *
     * @param rs      Il ResultSet posizionato sulla riga corrente.
     * @param utente  L'utente da associare inizialmente come autore/proprietario.
     * @param bacheca La bacheca di appartenenza.
     * @return Un'istanza di ToDo popolata con i dati del database.
     * @throws SQLException Se si verifica un errore durante la lettura del ResultSet.
     */
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

    /**
     * Sposta un ToDo da una bacheca all'altra.
     * (Attualmente non implementato).
     *
     * @param t   Il ToDo da spostare.
     * @param idB L'ID della nuova bacheca.
     * @param idT L'ID del ToDo.
     */
    @Override public void cambiabacheca(ToDo t, int idB, int idT) {}

    /**
     * Elimina tutti i ToDo di una bacheca.
     * (Attualmente non implementato).
     *
     * @param idB L'ID della bacheca da svuotare.
     */
    @Override public void svuotabahcea(int idB) {}

    /**
     * Recupera lo username dell'autore di un ToDo.
     * (Attualmente non implementato, ritorna sempre null).
     *
     * @param idT L'ID del ToDo.
     * @return Lo username dell'autore.
     */
    @Override public String getautore(int idT) { return null; }
}