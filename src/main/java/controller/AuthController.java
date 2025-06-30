package controller;

import dao.UserDAO;
import model.User;

public class AuthController {
    private final UserDAO userDao;

    public AuthController(UserDAO userDao) {
        this.userDao = userDao;
    }

    public boolean login(String username, String password) {
        try {
            return userDao.validateCredentials(username, password);
        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }
    }
}