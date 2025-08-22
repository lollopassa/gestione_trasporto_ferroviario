package controller;

import dao.ClienteDAO;
import domain.Cliente;
import exception.DAOException;

public class RegistrazioneController {

    /** Registrazione cliente: cf, username, password. */
    public Cliente registraCliente(String cf, String username, String password) throws DAOException {
        if (cf == null || cf.isBlank()) throw new IllegalArgumentException("CF obbligatorio.");
        if (username == null || username.isBlank()) throw new IllegalArgumentException("Username obbligatorio.");
        if (password == null || password.isBlank()) throw new IllegalArgumentException("Password obbligatoria.");
        return ClienteDAO.registraCliente(cf, username, password);
    }

}
