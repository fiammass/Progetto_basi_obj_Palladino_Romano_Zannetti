package postgresdao;

import dao.ChecklistDAO;
import model.ChecklistItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresChecklistDAO implements ChecklistDAO {

    @Override
    public void addItem(int todoId, ChecklistItem item) throws SQLException {
        String sql = "INSERT INTO checklist_items (description, completed, todo_id) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, item.getDescription());
            pstmt.setBoolean(2, item.isCompleted());
            pstmt.setInt(3, todoId);
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) item.setId(rs.getInt(1));
            }
            DatabaseConnection.commit();
        }
    }

    @Override
    public void toggleItem(int itemId) throws SQLException {
        String sql = "UPDATE checklist_items SET completed = NOT completed WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, itemId);
            pstmt.executeUpdate();
            DatabaseConnection.commit();
        }
    }

    @Override
    public void deleteItem(int itemId) throws SQLException {
        String sql = "DELETE FROM checklist_items WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, itemId);
            pstmt.executeUpdate();
            DatabaseConnection.commit();
        }
    }

    @Override
    public List<ChecklistItem> getItemsForTodo(int todoId) throws SQLException {
        String sql = "SELECT * FROM checklist_items WHERE todo_id = ? ORDER BY id";
        List<ChecklistItem> items = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, todoId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    items.add(mapResultSetToItem(rs));
                }
            }
        }
        return items;
    }

    @Override
    public boolean areAllItemsComplete(int todoId) throws SQLException {
        String sql = "SELECT COUNT(*) AS incomplete FROM checklist_items WHERE todo_id = ? AND completed = false";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, todoId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("incomplete") == 0;
                }
            }
        }
        return false;
    }

    private ChecklistItem mapResultSetToItem(ResultSet rs) throws SQLException {
        ChecklistItem item = new ChecklistItem();
        item.setId(rs.getInt("id"));
        item.setDescription(rs.getString("description"));
        item.setCompleted(rs.getBoolean("completed"));
        return item;
    }
}