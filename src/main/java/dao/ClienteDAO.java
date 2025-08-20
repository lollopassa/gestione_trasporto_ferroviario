// src/main/java/dao/ClienteDAO.java
package dao;

import domain.Cliente;
import exception.DAOException;
import utility.DBConnection;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public final class ClienteDAO {

    private static final String PROPS       = "loginuser.properties";
    private static final String SP_REGISTRA = "{CALL sp_registra_cliente(?,?,?,?)}";

    private ClienteDAO() { }


    public static Cliente registraCliente(String nome,
                                          String cognome,
                                          String username,
                                          String password) throws DAOException {
        try (Connection conn = DBConnection.getConnection(PROPS);
             CallableStatement cs = conn.prepareCall(SP_REGISTRA)) {

            cs.setString(1, nome);
            cs.setString(2, cognome);
            cs.setString(3, username);
            cs.setString(4, password);
            cs.execute();

            // se la SP non lancia eccezioni, restituiamo il Cliente creato
            return new Cliente(nome, cognome);

        } catch (SQLException e) {
            throw new DAOException("Errore registrazione cliente: " + e.getMessage(), e);
        }
    }
}