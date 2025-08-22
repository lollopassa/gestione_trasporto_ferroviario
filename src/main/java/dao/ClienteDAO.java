package dao;

import domain.Cliente;
import exception.DAOException;
import utility.DBConnection;

import java.sql.*;

public final class ClienteDAO {

    private static final String PROPS       = "loginuser.properties";
    // sp_registra_cliente(IN p_cf CHAR(16), IN p_user VARCHAR(20), IN p_pass VARCHAR(65))
    private static final String SP_REGISTRA = "{CALL sp_registra_cliente(?,?,?)}";
    private static final String SQL_CF_BY_USER = "SELECT cf FROM cliente WHERE username = ?";

    private ClienteDAO() { }

    public static Cliente registraCliente(String cf,
                                          String username,
                                          String password) throws DAOException {
        try (Connection conn = DBConnection.getConnection(PROPS);
             CallableStatement cs = conn.prepareCall(SP_REGISTRA)) {
            cs.setString(1, cf);
            cs.setString(2, username);
            cs.setString(3, password);
            cs.execute();
            return new Cliente(cf);
        } catch (SQLException e) {
            throw new DAOException("Errore registrazione cliente: " + e.getMessage(), e);
        }
    }

    /** Ricava il CF del cliente dalla username (tabella cliente con FK a credentials). */
    public static String findCfByUsername(String username) throws DAOException {
        try (Connection conn = DBConnection.getConnection(PROPS);
             PreparedStatement ps = conn.prepareStatement(SQL_CF_BY_USER)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString(1) : null;
            }
        } catch (SQLException e) {
            throw new DAOException("Errore lookup CF per username: " + e.getMessage(), e);
        }
    }
}
