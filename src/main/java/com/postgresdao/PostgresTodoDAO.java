package com.postgresdao;

import com.dao.TodoDAO;
import com.model.Todo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresTodoDAO implements TodoDAO {
    @Override
    public void create(Todo todo) throws SQLException {
        String sql = "INSERT INTO todos (title, description, due_date, board, completed, color, image_url) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, todo.getTitle());
            pstmt.setString(2, todo.getDescription());
            pstmt.setString(3, todo.getDueDate());
            pstmt.setString(4, todo.getBoard());
            pstmt.setBoolean(5, todo.isCompleted());
            pstmt.setString(6, todo.getColor());
            pstmt.setString(7, todo.getImageUrl());

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    todo.setId(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public Todo getById(int id) throws SQLException {
        String sql = "SELECT * FROM todos WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTodo(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Todo> getByBoard(String boardName) throws SQLException {
        String sql = "SELECT * FROM todos WHERE board = ? ORDER BY due_date";
        List<Todo> todos = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, boardName);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    todos.add(mapResultSetToTodo(rs));
                }
            }
        }
        return todos;
    }

    @Override
    public void update(Todo todo) throws SQLException {
        String sql = "UPDATE todos SET title = ?, description = ?, due_date = ?, " +
                "board = ?, completed = ?, color = ?, image_url = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, todo.getTitle());
            pstmt.setString(2, todo.getDescription());
            pstmt.setString(3, todo.getDueDate());
            pstmt.setString(4, todo.getBoard());
            pstmt.setBoolean(5, todo.isCompleted());
            pstmt.setString(6, todo.getColor());
            pstmt.setString(7, todo.getImageUrl());
            pstmt.setInt(8, todo.getId());

            pstmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM todos WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    @Override
    public List<Todo> getDueToday() throws SQLException {
        String sql = "SELECT * FROM todos WHERE due_date = CURRENT_DATE";
        return executeQuery(sql);
    }

    @Override
    public List<Todo> search(String query) throws SQLException {
        String sql = "SELECT * FROM todos WHERE title ILIKE ? OR description ILIKE ?";
        List<Todo> todos = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + query + "%");
            pstmt.setString(2, "%" + query + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    todos.add(mapResultSetToTodo(rs));
                }
            }
        }
        return todos;
    }

    // Helper methods
    private List<Todo> executeQuery(String sql) throws SQLException {
        List<Todo> todos = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                todos.add(mapResultSetToTodo(rs));
            }
        }
        return todos;
    }

    private Todo mapResultSetToTodo(ResultSet rs) throws SQLException {
        Todo todo = new Todo();
        todo.setId(rs.getInt("id"));
        todo.setTitle(rs.getString("title"));
        todo.setDescription(rs.getString("description"));
        todo.setDueDate(rs.getString("due_date"));
        todo.setBoard(rs.getString("board"));
        todo.setCompleted(rs.getBoolean("completed"));
        todo.setColor(rs.getString("color"));
        todo.setImageUrl(rs.getString("image_url"));
        return todo;
    }
}