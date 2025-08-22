package dao;

import exception.DAOException;
import utility.DBConnection;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class RegistroDAO {
    private static final String PROPS      = "personaleuser.properties";
    private static final String SP_SEGNALA = "{CALL sp_segnala_manutenzione(?,?,?,?)}";

    public void segnalaEvento(String cf,
                              String idTreno,
                              LocalDateTime dataEvento,
                              String descrizione) throws DAOException {
        try (Connection conn = DBConnection.getConnection(PROPS);
             CallableStatement cs = conn.prepareCall(SP_SEGNALA)) {
            cs.setString(1, cf);
            cs.setString(2, idTreno);
            cs.setTimestamp(3, dataEvento != null ? Timestamp.valueOf(dataEvento) : null);
            cs.setString(4, descrizione);
            cs.execute();
        } catch (SQLException e) {
            throw new DAOException("Errore inserimento nel registro: " + e.getMessage(), e);
        }
    }
}
