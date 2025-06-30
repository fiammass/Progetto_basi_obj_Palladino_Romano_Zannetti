package dao;

import model.User;
import java.sql.SQLException;
import java.util.Optional;

public interface UserDAO {
    void createUser(User user) throws SQLException;
    Optional<User> getUserByUsername(String username) throws SQLException;
    boolean validateCredentials(String username, String password) throws SQLException;
    void updateUser(User user) throws SQLException;
    void deleteUser(int userId) throws SQLException;
}