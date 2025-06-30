package dao;

import java.sql.SQLException;
import java.util.List;

public interface SharedUserDAO {
    void shareTodoWithUser(int todoId, int userId) throws SQLException;
    void unshareTodoWithUser(int todoId, int userId) throws SQLException;
    List<Integer> getSharedUsersForTodo(int todoId) throws SQLException;
    boolean isTodoSharedWithUser(int todoId, int userId) throws SQLException;
}