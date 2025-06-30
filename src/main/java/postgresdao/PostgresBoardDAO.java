package postgresdao;

import dao.BoardDAO;
import model.Board;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresBoardDAO implements BoardDAO {

    @Override
    public List<Board> getDefaultBoards() throws SQLException {
        String sql = "SELECT * FROM boards WHERE type IN ('UNIVERSITA', 'LAVORO', 'TEMPO_LIBERO')";
        return executeBoardQuery(sql);
    }

    @Override
    public Board getById(int id) throws SQLException {
        String sql = "SELECT * FROM boards WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBoard(rs);
                }
            }
        }
        return null;
    }

    @Override
    public void update(Board board) throws SQLException {
        String sql = "UPDATE boards SET title = ?, description = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, board.getTitle());
            pstmt.setString(2, board.getDescription());
            pstmt.setInt(3, board.getId());
            pstmt.executeUpdate();
            DatabaseConnection.commit();
        }
    }

    private List<Board> executeBoardQuery(String sql) throws SQLException {
        List<Board> boards = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                boards.add(mapResultSetToBoard(rs));
            }
        }
        return boards;
    }

    private Board mapResultSetToBoard(ResultSet rs) throws SQLException {
        Board board = new Board();
        board.setId(rs.getInt("id"));
        board.setTitle(rs.getString("title"));
        board.setDescription(rs.getString("description"));
        return board;
    }
}