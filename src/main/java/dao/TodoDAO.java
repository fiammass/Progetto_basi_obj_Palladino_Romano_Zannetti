package dao;

import model.Todo;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public interface TodoDAO {
    void createTodo(Todo todo) throws SQLException;
    void updateTodo(Todo todo) throws SQLException;
    void deleteTodo(int todoId) throws SQLException;
    Todo getTodoById(int todoId) throws SQLException;
    List<Todo> getTodosByBoard(int boardId) throws SQLException;

    // Funzionalità specifiche dalla traccia
    void moveTodoToBoard(int todoId, int newBoardId) throws SQLException;
    void updateTodoPosition(int todoId, int newPosition) throws SQLException;
    List<Todo> getTodosDueToday(int userId) throws SQLException;
    List<Todo> getTodosDueBeforeDate(int userId, Date date) throws SQLException;
    List<Todo> searchTodos(int userId, String query) throws SQLException;
}