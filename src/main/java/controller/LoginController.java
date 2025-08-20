package controller;

import dao.LoginDAO;
import domain.Role;

import java.sql.SQLException;

public final class LoginController {

    private LoginController() { }

    public static Role login(String username, String password) {

        try {
            Role r = LoginDAO.login(username, password);

            if (r == null) {
                System.out.println("❌  Login fallito: credenziali errate.\n");
            } else {
                System.out.printf("%n✅  Accesso effettuato come %s%n%n", r);
            }
            return r;

        } catch (SQLException e) {
            System.err.println("Errore login: " + e.getMessage());
            return null;
        }
    }
}
