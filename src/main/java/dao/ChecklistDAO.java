package dao;

import model.ChecklistItem;
import java.sql.SQLException;
import java.util.List;

public interface ChecklistDAO {
    void addChecklistItem(int todoId, ChecklistItem item) throws SQLException;
    void toggleChecklistItem(int itemId) throws SQLException;
    void deleteChecklistItem(int itemId) throws SQLException;
    List<ChecklistItem> getChecklistForTodo(int todoId) throws SQLException;
}