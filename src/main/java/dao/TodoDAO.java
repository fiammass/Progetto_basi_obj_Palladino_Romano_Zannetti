package dao;

import model.Todo;
import java.sql.SQLException;
import java.util.List;

public interface TodoDAO {
    void create(Todo todo) throws SQLException;
    Todo getById(int id) throws SQLException;
    List<Todo> getByBoard(String boardName) throws SQLException;
    void update(Todo todo) throws SQLException;
    void delete(int id) throws SQLException;
    List<Todo> getDueToday() throws SQLException;
    List<Todo> search(String query) throws SQLException;
}