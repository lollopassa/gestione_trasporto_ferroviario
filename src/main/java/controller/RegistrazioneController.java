package controller;

import dao.ClienteDAO;
import dao.PersonaleDAO;
import domain.Cliente;
import domain.Personale;
import domain.Personale.TipoPersonale;
import exception.DAOException;

public class RegistrazioneController {
    public Cliente registraCliente(String nome, String cognome, String username, String password) throws DAOException {
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("Nome obbligatorio.");
        if (cognome == null || cognome.isBlank()) throw new IllegalArgumentException("Cognome obbligatorio.");
        if (username == null || username.isBlank()) throw new IllegalArgumentException("Username obbligatorio.");
        if (password == null || password.isBlank()) throw new IllegalArgumentException("Password obbligatoria.");
        return ClienteDAO.registraCliente(nome, cognome, username, password);
    }
    public Personale registraPersonale(String nome, String cognome, TipoPersonale tipo, String username, String password)
            throws DAOException {
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("Nome obbligatorio.");
        if (cognome == null || cognome.isBlank()) throw new IllegalArgumentException("Cognome obbligatorio.");
        if (tipo == null) throw new IllegalArgumentException("Tipo personale obbligatorio.");
        if (username == null || username.isBlank()) throw new IllegalArgumentException("Username obbligatorio.");
        if (password == null || password.isBlank()) throw new IllegalArgumentException("Password obbligatoria.");
        return PersonaleDAO.registraPersonale(nome, cognome, tipo, username, password);
    }
}
