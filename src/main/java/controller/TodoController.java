package controller;

import dao.TodoDAO;
import model.Todo;
import java.sql.SQLException;
import java.util.List;

public class TodoController {
    private final TodoDAO todoDao;

    public TodoController(TodoDAO todoDao) {
        this.todoDao = todoDao;
    }

    // Operazioni CRUD
    public void addTodo(Todo todo) throws SQLException {
        if (todo.getTitle() == null || todo.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Il titolo è obbligatorio");
        }
        todoDao.create(todo);
    }

    public void updateTodo(Todo todo) throws SQLException {
        if (todo.getId() <= 0) {
            throw new IllegalArgumentException("ID todo non valido");
        }
        todoDao.update(todo);
    }

    public void deleteTodo(int todoId) throws SQLException {
        if (todoId <= 0) {
            throw new IllegalArgumentException("ID todo non valido");
        }
        todoDao.delete(todoId);
    }

    // Operazioni specifiche
    public List<Todo> getTodosByBoard(String boardName) throws SQLException {
        return todoDao.getByBoard(boardName);
    }

    public void moveTodoToBoard(int todoId, String newBoardName) throws SQLException {
        Todo todo = todoDao.getById(todoId);
        if (todo != null) {
            todo.setBoard(newBoardName);
            todoDao.update(todo);
        }
    }

    public List<Todo> getDueToday() throws SQLException {
        return todoDao.getDueToday();
    }

    public List<Todo> searchTodos(String query) throws SQLException {
        return todoDao.search(query);
    }

    public void toggleCompleteStatus(int todoId) throws SQLException {
        Todo todo = todoDao.getById(todoId);
        if (todo != null) {
            todo.setCompleted(!todo.isCompleted());
            todoDao.update(todo);
        }
    }
}