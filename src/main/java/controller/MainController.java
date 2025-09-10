package controller;

import dao.AuthDao;
import domain.Cliente;
import domain.Ruolo;
import exception.DAOException;

import java.sql.SQLException;

public class MainController {
    private final AuthDao authDao;

    public MainController(String propertiesFile) {
        this.authDao = new AuthDao(propertiesFile);
    }

    public static class EsitoLogin {
        public final String cf;
        public final Ruolo ruolo;
        public EsitoLogin(String cf, Ruolo ruolo) { this.cf = cf; this.ruolo = ruolo; }
        @Override public String toString() { return "EsitoLogin{cf='" + cf + "', ruolo=" + ruolo + '}'; }
    }

    public EsitoLogin login(String username, String password) throws DAOException {
        if (username == null || username.isBlank() || password == null) {
            throw new IllegalArgumentException("Username/password mancanti");
        }
        try {
            AuthDao.EsitoLogin esito = authDao.login(username, password);
            if (esito == null) return null;
            return new EsitoLogin(esito.cf, esito.ruolo);
        } catch (IllegalArgumentException e) {
            throw new DAOException("Ruolo non riconosciuto", e);
        } catch (SQLException e) {
            throw new DAOException("Errore login", e);
        }
    }

    public Cliente registraCliente(String cf, String nome, String cognome,
                                   String username, String password) throws DAOException {
        if (cf == null || cf.length() != 16) throw new IllegalArgumentException("CF deve essere di 16 caratteri");
        if (nome == null || nome.isBlank() || cognome == null || cognome.isBlank())
            throw new IllegalArgumentException("Nome e cognome obbligatori");
        if (username == null || username.isBlank() || password == null || password.isBlank())
            throw new IllegalArgumentException("Username e password obbligatori");

        try {
            return authDao.registraCliente(cf, nome, cognome, username, password);
        } catch (SQLException e) {
            throw new DAOException("Errore registrazione cliente", e);
        }
    }

    public void registraPersonale(String cf, String nome, String cognome,
                                  String username, String password, String tipo) throws DAOException {
        if (cf == null || cf.length() != 16) throw new IllegalArgumentException("CF deve essere di 16 caratteri");
        if (nome == null || nome.isBlank() || cognome == null || cognome.isBlank())
            throw new IllegalArgumentException("Nome e cognome obbligatori");
        if (username == null || username.isBlank() || password == null || password.isBlank())
            throw new IllegalArgumentException("Username e password obbligatori");
        if (tipo == null || !(tipo.equalsIgnoreCase("MACCHINISTA") || tipo.equalsIgnoreCase("CAPOTRENO")))
            throw new IllegalArgumentException("Tipo personale non valido (MACCHINISTA o CAPOTRENO)");

        try {
            authDao.registraPersonale(cf, nome, cognome, username, password, tipo.toUpperCase());
        } catch (SQLException e) {
            throw new DAOException("Errore registrazione personale", e);
        }
    }
}
