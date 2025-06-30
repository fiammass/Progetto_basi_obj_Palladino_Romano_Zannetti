package com.todomanager.controller;

import com.todomanager.dao.UserDAO;
import com.todomanager.model.User;

public class AuthController {
    private final UserDAO userDao;

    public AuthController() {
        this.userDao = new PostgresUserDAO(); // Implementazione concreta
    }

    public boolean login(String username, String password) throws Exception {
        User user = userDao.findByUsername(username);
        return user != null && user.getPassword().equals(password);
    }
}