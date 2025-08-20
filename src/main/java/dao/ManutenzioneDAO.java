// src/main/java/dao/ManutenzioneDAO.java
package dao;

import exception.DAOException;
import utility.DBConnection;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class ManutenzioneDAO {

    private static final String PROPS      = "personaleuser.properties";
    private static final String SP_SEGNALA = "{CALL sp_segnala_manutenzione(?,?,?,?,?)}";

    public void segnalaManutenzione(String matricola,
                                    String marca,
                                    String modello,
                                    LocalDateTime dataEvento,
                                    String descrizione) throws DAOException {
        try (Connection conn = DBConnection.getConnection(PROPS);
             CallableStatement cs = conn.prepareCall(SP_SEGNALA)) {

            cs.setString(1, matricola);
            cs.setString(2, marca);
            cs.setString(3, modello);
            cs.setTimestamp(4, Timestamp.valueOf(dataEvento));
            cs.setString(5, descrizione);
            cs.execute();

        } catch (SQLException e) {
            throw new DAOException(
                    "Errore inserimento segnalazione manutenzione: " + e.getMessage(), e);
        }
    }
}
