package dao;

import model.Board;
import java.sql.SQLException;
import java.util.List;

public interface BoardDAO {
    List<Board> getDefaultBoards() throws SQLException; // Università/Lavoro/Tempo Libero
    Board getBoardById(int boardId) throws SQLException;
    List<Board> getUserBoards(int userId) throws SQLException;
    void updateBoard(Board board) throws SQLException;
}