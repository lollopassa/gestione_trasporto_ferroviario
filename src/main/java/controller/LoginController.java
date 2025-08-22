package controller;

import domain.Role;
import dao.LoginDAO;

import java.sql.SQLException;

public class LoginController {
    public Role login(String username, String password) throws SQLException {
        if (username == null || username.isBlank()) throw new IllegalArgumentException("Username obbligatorio.");
        if (password == null || password.isBlank()) throw new IllegalArgumentException("Password obbligatoria.");
        return LoginDAO.login(username, password);
    }
}
