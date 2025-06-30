package postgresdao;

import dao.SharedUserDAO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresSharedUserDAO implements SharedUserDAO {

    @Override
    public void share(int todoId, int userId) throws SQLException {
        String sql = "INSERT INTO shared_todos (todo_id, user_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, todoId);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
            DatabaseConnection.commit();
        }
    }

    @Override
    public void unshare(int todoId, int userId) throws SQLException {
        String sql = "DELETE FROM shared_todos WHERE todo_id = ? AND user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, todoId);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
            DatabaseConnection.commit();
        }
    }

    @Override
    public List<Integer> getSharedUsers(int todoId) throws SQLException {
        String sql = "SELECT user_id FROM shared_todos WHERE todo_id = ?";
        List<Integer> userIds = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, todoId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    userIds.add(rs.getInt("user_id"));
                }
            }
        }
        return userIds;
    }

    @Override
    public boolean isSharedWithUser(int todoId, int userId) throws SQLException {
        String sql = "SELECT 1 FROM shared_todos WHERE todo_id = ? AND user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, todoId);
            pstmt.setInt(2, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }
}