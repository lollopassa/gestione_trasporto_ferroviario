// src/main/java/dao/PersonaleDAO.java
package dao;

import domain.Personale;
import domain.Personale.TipoPersonale;
import exception.DAOException;
import utility.DBConnection;

import java.sql.*;

public final class PersonaleDAO {

    // Per registrazione (creazione credenziali + inserimento in personale)
    private static final String PROPS_WRITE = "loginuser.properties";
    private static final String SP_REGISTRA = "{CALL sp_registra_personale(?,?,?,?,?)}";

    // Per lettura dati personali in sessione
    private static final String PROPS_READ = "personaleuser.properties";
    private static final String SQL_FIND_BY_USERNAME =
            "SELECT nome, cognome, tipo FROM personale WHERE username = ?";

    private PersonaleDAO() { }

    /**
     * Chiama la stored procedure per registrare un nuovo addetto.
     * @return l’oggetto Personale appena creato
     */
    public static Personale registraPersonale(String nome,
                                              String cognome,
                                              TipoPersonale tipo,
                                              String username,
                                              String password) throws DAOException {
        try (Connection conn = DBConnection.getConnection(PROPS_WRITE);
             CallableStatement cs = conn.prepareCall(SP_REGISTRA)) {

            cs.setString(1, nome);
            cs.setString(2, cognome);
            cs.setString(3, tipo.name());
            cs.setString(4, username);
            cs.setString(5, password);
            cs.execute();

            return new Personale(nome, cognome, tipo);

        } catch (SQLException e) {
            throw new DAOException("Errore registrazione personale: " + e.getMessage(), e);
        }
    }

    /**
     * Recupera nome, cognome e tipo di un addetto dal suo username.
     * @throws DAOException se l’username non esiste o su errore di accesso
     */
    public static Personale getByUsername(String username) throws DAOException {
        try (Connection conn = DBConnection.getConnection(PROPS_READ);
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_USERNAME)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String nome    = rs.getString("nome");
                    String cognome = rs.getString("cognome");
                    TipoPersonale tipo = TipoPersonale.valueOf(rs.getString("tipo"));
                    return new Personale(nome, cognome, tipo);
                } else {
                    throw new DAOException("Personale non trovato per username: " + username);
                }
            }

        } catch (SQLException e) {
            throw new DAOException("Errore lettura dati personale: " + e.getMessage(), e);
        }
    }
}
