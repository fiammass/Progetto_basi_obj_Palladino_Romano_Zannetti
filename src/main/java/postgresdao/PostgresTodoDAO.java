package postgresdao;

import dao.TodoDAO;
import model.Todo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresTodoDAO implements TodoDAO {

    @Override
    public void create(Todo todo) throws SQLException {
        String sql = "INSERT INTO todos (title, description, due_date, image_url, color, completed, position, board_id, user_id) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            setTodoParameters(pstmt, todo);
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) todo.setId(rs.getInt(1));
            }
            DatabaseConnection.commit();
        }
    }

    @Override
    public void update(Todo todo) throws SQLException {
        String sql = "UPDATE todos SET title=?, description=?, due_date=?, image_url=?, color=?, completed=?, position=?, board_id=? WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            setTodoParameters(pstmt, todo);
            pstmt.setInt(9, todo.getId());
            pstmt.executeUpdate();
            DatabaseConnection.commit();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM todos WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            DatabaseConnection.commit();
        }
    }

    @Override
    public Todo getById(int id) throws SQLException {
        String sql = "SELECT * FROM todos WHERE id=?";
        Todo todo = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    todo = mapResultSetToTodo(rs);
                }
            }
        }
        return todo;
    }

    @Override
    public List<Todo> getByBoard(int boardId) throws SQLException {
        String sql = "SELECT * FROM todos WHERE board_id=? ORDER BY position";
        List<Todo> todos = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, boardId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    todos.add(mapResultSetToTodo(rs));
                }
            }
        }
        return todos;
    }

    @Override
    public void moveToBoard(int todoId, int newBoardId) throws SQLException {
        String sql = "UPDATE todos SET board_id=? WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, newBoardId);
            pstmt.setInt(2, todoId);
            pstmt.executeUpdate();
            DatabaseConnection.commit();
        }
    }

    @Override
    public void updatePosition(int todoId, int newPosition) throws SQLException {
        String sql = "UPDATE todos SET position=? WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, newPosition);
            pstmt.setInt(2, todoId);
            pstmt.executeUpdate();
            DatabaseConnection.commit();
        }
    }

    @Override
    public List<Todo> getDueToday(int userId) throws SQLException {
        String sql = "SELECT * FROM todos WHERE user_id=? AND due_date::date = CURRENT_DATE";
        return executeDateQuery(sql, userId);
    }

    @Override
    public List<Todo> getDueBeforeDate(int userId, Date date) throws SQLException {
        String sql = "SELECT * FROM todos WHERE user_id=? AND due_date::date <= ?";
        List<Todo> todos = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setDate(2, new java.sql.Date(date.getTime()));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    todos.add(mapResultSetToTodo(rs));
                }
            }
        }
        return todos;
    }

    @Override
    public List<Todo> search(String query, int userId) throws SQLException {
        String sql = "SELECT * FROM todos WHERE user_id=? AND (title ILIKE ? OR description ILIKE ?)";
        List<Todo> todos = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, "%" + query + "%");
            pstmt.setString(3, "%" + query + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    todos.add(mapResultSetToTodo(rs));
                }
            }
        }
        return todos;
    }

    // === Metodi di supporto ===
    private void setTodoParameters(PreparedStatement pstmt, Todo todo) throws SQLException {
        pstmt.setString(1, todo.getTitle());
        pstmt.setString(2, todo.getDescription());
        pstmt.setTimestamp(3, todo.getDueDate() != null ? new Timestamp(todo.getDueDate().getTime()) : null);
        pstmt.setString(4, todo.getImageUrl());
        pstmt.setString(5, todo.getColor());
        pstmt.setBoolean(6, todo.isCompleted());
        pstmt.setInt(7, todo.getPosition());
        pstmt.setInt(8, todo.getBoardId());
        pstmt.setInt(9, todo.getUserId());
    }

    private Todo mapResultSetToTodo(ResultSet rs) throws SQLException {
        Todo todo = new Todo();
        todo.setId(rs.getInt("id"));
        todo.setTitle(rs.getString("title"));
        todo.setDescription(rs.getString("description"));
        todo.setDueDate(rs.getTimestamp("due_date"));
        todo.setImageUrl(rs.getString("image_url"));
        todo.setColor(rs.getString("color"));
        todo.setCompleted(rs.getBoolean("completed"));
        todo.setPosition(rs.getInt("position"));
        todo.setBoardId(rs.getInt("board_id"));
        todo.setUserId(rs.getInt("user_id"));
        return todo;
    }

    private List<Todo> executeDateQuery(String sql, int userId) throws SQLException {
        List<Todo> todos = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    todos.add(mapResultSetToTodo(rs));
                }
            }
        }
        return todos;
    }
}